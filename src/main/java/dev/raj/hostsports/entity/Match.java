package dev.raj.hostsports.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "tournament_id", nullable = false)
    @ToString.Exclude
    private Tournament tournament;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "team_a_id", nullable = false)
    @ToString.Exclude
    private Team teamA;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_b_id",nullable = false)
    @ToString.Exclude
    private Team teamB;

    @Column(nullable = false)
    private LocalDateTime matchDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venu_id")
    @ToString.Exclude
    private Venue venue;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private MatchStatus status = MatchStatus.SCHEDULED;

    @Builder.Default
    @Column(nullable = false)
    private Integer scoreTeamA = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer scoreTeamB = 0;

    @Enumerated(EnumType.STRING)
    private MatchResult result;

    @Version
    private Long version;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private  LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
