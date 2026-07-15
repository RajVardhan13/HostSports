package dev.raj.hostsports.dto.payment;

import dev.raj.hostsports.entity.PaymentMode;
import dev.raj.hostsports.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private Long bookingId;
    private String  transactionRef;
    private Double amount;
    private PaymentStatus status;
    private PaymentMode paymentMethod;
    private LocalDateTime createdAt;
}
