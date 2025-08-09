package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cards")
@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "encrypted_card_number", nullable = false, unique = true)
    private String encryptedCardNumber;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;
    @Enumerated(EnumType.STRING)
    private CardStatus status;
    private BigDecimal balance;
}
