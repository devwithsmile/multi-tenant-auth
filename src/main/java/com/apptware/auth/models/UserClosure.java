package com.apptware.auth.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_closure")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserClosure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The ancestor (higher-up) user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ancestor_id", nullable = false)
    private User ancestor;

    // The descendant (subordinate) user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "descendant_id", nullable = false)
    private User descendant;

    // The depth of the relationship (0 for self, 1 for direct report, etc.)
    @Column(nullable = false)
    private int depth;
}
