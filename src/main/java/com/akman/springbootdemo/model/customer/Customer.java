package com.akman.springbootdemo.model.customer;

import com.akman.springbootdemo.model.account.Account;
import com.akman.springbootdemo.model.credit.Credit;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"accountList"})
@Table(name = "CUSTOMER", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "last_name"})
})
public class Customer {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Account> accountList;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Credit> creditList;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "day_of_birth")
    private LocalDate dayOfBirth;

    @Column(name = "address")
    private String address;

    @Column(name = "rating")
    private int rating;
}