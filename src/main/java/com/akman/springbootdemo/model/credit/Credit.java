package com.akman.springbootdemo.model.credit;

import com.akman.springbootdemo.model.customer.Customer;
import com.akman.springbootdemo.model.enums.Currency;
import com.akman.springbootdemo.model.transaction.Transaction;
import com.akman.springbootdemo.utils.Constants;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"incomingTransactionList", "customer"})
@Table(name = "CREDIT")
public class Credit {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @OneToMany(mappedBy = "toCredit", cascade = CascadeType.ALL)
    private List<Transaction> incomingTransactionList;

    @Column(name = "credit_amount")
    private BigDecimal creditAmount;

    @Column(name = "remaining_credit_amount")
    private BigDecimal remainingCreditAmount;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "original_term")
    private Long term;

    @Column(name = "remaining_term")
    private Long remainingTerm;

    @Column(name = "created_date_time")
    private LocalDateTime createdDateTime;

    @Column(name = "is_active")
    private boolean isActive;

    @PrePersist
    public void initSavRemainingCreditAmount() {
        if (createdDateTime == null) {
            remainingTerm = term;
            remainingCreditAmount = creditAmount;
            createdDateTime = Constants.weekDayDateTimeNow();
        }
    }

    @PreUpdate
    @PostLoad
    public void initRemainingCreditAmount() {
        if (isActive) {
            remainingTerm = term - ChronoUnit.DAYS.between(createdDateTime, LocalDateTime.now());
            if (remainingTerm <= 0) {
                if (BigDecimal.ZERO.compareTo(remainingCreditAmount) <= 0) {
                    int rating = customer.getRating();
                    customer.setRating(rating < 4 ? rating + 1 : 4);
                }
                isActive = false;
            }
        }
    }
}