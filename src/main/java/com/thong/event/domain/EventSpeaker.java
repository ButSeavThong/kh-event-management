package com.thong.event.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(
    name = "event_speaker",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"event_id", "speaker_id"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventSpeaker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speaker_id", nullable = false)
    private Speaker speaker;


    // Optional metadata
    private Integer displayOrder;       // Order to display (1, 2, 3...)
    private String role;                // "Keynote", "Panelist", "Workshop Leader"
    private LocalTime speakingTime;     // e.g., "14:30" for 2:30 PM
    private BigDecimal speakerFee;      // How much they're paid
}
