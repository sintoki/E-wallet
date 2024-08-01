package org.demo.TransactionService.Model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.util.Date;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Getter
public class Txn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    private String txnId;
    private String sender;
    private String receiver;
    private Double amount;
    private String purpose;
    @Enumerated(value =EnumType.STRING)
    private TxnStatus txnStatus;

}
