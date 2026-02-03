package com.thong.event.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "speakers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Speaker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    private String title;
    private String company;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String imageUrl;

    @OneToMany(
        mappedBy = "speaker",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<EventSpeaker> events;
}
