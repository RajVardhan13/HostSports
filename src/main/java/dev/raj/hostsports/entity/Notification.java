package dev.raj.hostsports.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications",
        uniqueConstraints = @UniqueConstraint(columnNames = "eventKey"))
@Getter
@Setter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String type;

    private String message;

    private String eventKey;

    @Column(name = "is_read")
    private boolean read;

    private LocalDateTime createdAt = LocalDateTime.now();
}