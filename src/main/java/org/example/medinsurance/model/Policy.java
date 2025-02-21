package org.example.medinsurance.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.medinsurance.enums.PolicyStatus;

@Entity
@Table(name = "policies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double percentage; // Coverage percentage

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PolicyStatus status;
}
