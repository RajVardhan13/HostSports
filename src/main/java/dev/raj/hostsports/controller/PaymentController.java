package dev.raj.hostsports.controller;

import com.razorpay.RazorpayException;
import dev.raj.hostsports.dto.payment.PaymentRequest;
import dev.raj.hostsports.dto.payment.PaymentResponse;
import dev.raj.hostsports.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.Map;
import dev.raj.hostsports.dto.payment.VerifyPaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Simulated payment processing for bookings")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> pay(@Valid @RequestBody PaymentRequest request,
                                               @AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.ok(paymentService.processPayment(request, currentUser));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentResponse> getByBooking(@PathVariable Long bookingId,
                                                        @AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.ok(paymentService.getPaymentByBookingId(bookingId, currentUser));
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(
            @Valid @RequestBody VerifyPaymentRequest request) throws RazorpayException {

        boolean isValid = paymentService.verifyPayment(
                request.getOrderId(),
                request.getPaymentId(),
                request.getSignature()
        );

        if (isValid) {
            return ResponseEntity.ok("Payment verified");
        }

        return ResponseEntity.badRequest()
                .body("Invalid payment signature");
    }
}
