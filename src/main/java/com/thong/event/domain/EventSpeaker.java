package com.thong.event.domain;

import jakarta.persistence.*;
import lombok.*;

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
}
