package com.akman.springbootdemo.model.account;

import com.akman.springbootdemo.model.customer.Customer;
import com.akman.springbootdemo.model.enums.Currency;
import com.akman.springbootdemo.model.transaction.Transaction;
import com.akman.springbootdemo.utils.Constants;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"outgoingTransactionList", "incomingTransactionList"})
@Table(name = "ACCOUNT")
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL)
    private List<Transaction> outgoingTransactionList;

    @OneToMany(mappedBy = "toAccount", cascade = CascadeType.ALL)
    private List<Transaction> incomingTransactionList;

    @Column(name = "no")
    private Long no;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "created_date_time")
    private LocalDateTime createdDateTime;

    @PrePersist
    public void initBalance() {
        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
        createdDateTime = Constants.weekDayDateTimeNow();
    }
}
