package dev.raj.hostsports.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    @ToString.Exclude
    private User player;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "slot_id", nullable = false, unique = true)
    @ToString.Exclude
    private Slot slot;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    @Column(nullable = false)
    private Double totalAmount;

    @Column(updatable = false)
    private LocalDateTime bookedAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        this.bookedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
