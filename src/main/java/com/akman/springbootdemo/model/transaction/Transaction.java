package com.akman.springbootdemo.model.transaction;

import com.akman.springbootdemo.model.account.Account;
import com.akman.springbootdemo.model.credit.Credit;
import com.akman.springbootdemo.model.enums.Currency;
import com.akman.springbootdemo.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "TRANSACTION")
public class Transaction {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "from_account_id", referencedColumnName = "id")
    private Account fromAccount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "to_account_id", referencedColumnName = "id")
    private Account toAccount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "to_credit_id", referencedColumnName = "id")
    private Credit toCredit;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "is_credit_payment")
    private boolean isCreditPayment;

    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;

    @PrePersist
    public void init() {
        transactionTime = Constants.weekDayDateTimeNow();
    }
}
