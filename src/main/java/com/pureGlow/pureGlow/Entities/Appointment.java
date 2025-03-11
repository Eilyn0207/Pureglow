package com.pureGlow.pureGlow.Entities;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ToString(exclude ={"client"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "quotes")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private String description;

    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name = "id_client", nullable = false)
    @JsonIncludeProperties({"id", "user"})
    private Client client;

    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name = "id_trainer", nullable = false)
    @JsonIncludeProperties({"id", "user"})
    private Trainer trainer;
}
