package dev.raj.hostsports.service;

import com.razorpay.RazorpayException;
import dev.raj.hostsports.dto.payment.PaymentRequest;
import dev.raj.hostsports.dto.payment.PaymentResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request, UserDetails currentUser);
    PaymentResponse getPaymentByBookingId(Long bookingId, UserDetails currentUser);

    boolean verifyPayment(String orderId,String paymentId, String signature) throws RazorpayException;
}
