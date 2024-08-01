package org.demo.WalletService.Model;

import lombok.*;
import org.demo.utils.UserIdentifier;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private String contact;
    @Enumerated(value = EnumType.ORDINAL)
    private UserIdentifier userIdentifier;

    private String userIdentifierValue;

    private Double balance;
    @UpdateTimestamp
    private Date updatedAt;
    @CreationTimestamp
    private Date createdAt;

}