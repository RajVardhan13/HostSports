package dev.raj.hostsports.service.impl;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import dev.raj.hostsports.dto.payment.PaymentRequest;
import dev.raj.hostsports.dto.payment.PaymentResponse;
import dev.raj.hostsports.entity.*;
import dev.raj.hostsports.exception.DuplicateResourceException;
import dev.raj.hostsports.exception.ResourceNotFoundException;
import dev.raj.hostsports.mapper.PaymentMapper;
import dev.raj.hostsports.repository.BookingRepository;
import dev.raj.hostsports.repository.PaymentRepository;
import dev.raj.hostsports.repository.SlotRepository;
import dev.raj.hostsports.repository.UserRepository;
import dev.raj.hostsports.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
//import java.util.concurrent.ThreadLocalRandom;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final SlotRepository slotRepository;
    private final UserRepository userRepository;
    private final PaymentMapper paymentMapper;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request, UserDetails currentUser) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + request.getBookingId()));

        User user = resolveUser(currentUser);
        boolean isOwner = booking.getPlayer().getId().equals(user.getId());
        boolean isAdmin = user.getRole().name().equals("ADMIN");

        if(!isOwner && !isAdmin){
            throw new AccessDeniedException("You do not own this booking");
        }

        if(booking.getStatus() != BookingStatus.PENDING){
            throw new DuplicateResourceException("Payment can only be made for PENDING Booking");
        }

        if(paymentRepository.findByBookingId(booking.getId()).isPresent()){
            throw new DuplicateResourceException("A payment already exists for this booking");
        }

//        boolean success = ThreadLocalRandom.current().nextInt(100) < 90;
//
//        Payment payment = Payment.builder()
//                .booking(booking)
//                .amount(booking.getTotalAmount())
//                .paymentMethod(request.getPaymentMethod())
//                .status(success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED)
//                .build();

//        Payment saved = paymentRepository.save(payment);
//
//        if(success){
//            booking.setStatus(BookingStatus.CONFIRMED);
//        }else{
//            booking.setStatus(BookingStatus.FAILED);
//
//            Slot slot = booking.getSlot();
//            slot.setAvailable(true);
//            slotRepository.save(slot);
//        }
//        bookingRepository.save(booking);
//
//        return paymentMapper.toResponse(saved);
        try {

            String receiptId = "BOOKING_" + booking.getId();

            String razorpayOrderId = createOrder(
                    BigDecimal.valueOf(booking.getTotalAmount()),
                    receiptId
            );

            Payment payment = Payment.builder()
                    .booking(booking)
                    .amount(booking.getTotalAmount())
                    .paymentMethod(request.getPaymentMethod())
                    .status(PaymentStatus.PENDING)
                    .razorpayOrderId(razorpayOrderId)
                    .build();

            Payment saved = paymentRepository.save(payment);

            return paymentMapper.toResponse(saved);

        } catch (RazorpayException e) {

            throw new RuntimeException("Failed to create Razorpay order", e);
        }
    }

    private User resolveUser(UserDetails currentUser) {
        return userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public PaymentResponse getPaymentByBookingId(Long bookingId, UserDetails currentUser) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("No payment found for booking id: " + bookingId));

        User user = resolveUser(currentUser);
        boolean isOwner = payment.getBooking().getPlayer().getId().equals(user.getId());
        boolean isAdmin = user.getRole().name().equals("ADMIN");
        if (!isOwner && !isAdmin){
            throw new AccessDeniedException("You do not this payment");
        }

        return paymentMapper.toResponse(payment);
    }

    public String createOrder(BigDecimal amount, String receiptId) throws RazorpayException {

        RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

        JSONObject orderRequest = new JSONObject();

        //orderRequest.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValueExact());
        orderRequest.put("amount", amount.setScale(2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).intValue());
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", receiptId);

        Order order = client.orders.create(orderRequest);

        return order.get("id");
    }

    public boolean verifyPayment(String orderId, String paymentId,String signature) throws RazorpayException {

        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", orderId);
        options.put("razorpay_payment_id", paymentId);
        options.put("razorpay_signature", signature);

        boolean isValid = Utils.verifyPaymentSignature(options, razorpayKeySecret);

        Payment payment = paymentRepository.findByRazorpayOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("No payment found for order id: " + orderId));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            return payment.getStatus() == PaymentStatus.SUCCESS;
        }

        if (isValid) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setRazorpayPaymentId(paymentId);
            payment.setRazorpaySignature(signature);
            paymentRepository.save(payment);

            Booking booking = payment.getBooking();
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);

            Booking booking = payment.getBooking();
            booking.setStatus(BookingStatus.FAILED);
            Slot slot = booking.getSlot();
            slot.setAvailable(true);
            slotRepository.save(slot);
            bookingRepository.save(booking);
        }

        return isValid;
    }
}
