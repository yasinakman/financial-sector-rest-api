package com.akman.springbootdemo.rest_api;

import com.akman.springbootdemo.model.account.AccountRequest;
import com.akman.springbootdemo.model.credit.CreditRequest;
import com.akman.springbootdemo.model.customer.CustomerRequest;
import com.akman.springbootdemo.model.enums.Currency;
import com.akman.springbootdemo.model.transaction.TransactionRequest;
import com.akman.springbootdemo.model.user.User;
import com.akman.springbootdemo.repository.UserRepository;
import com.akman.springbootdemo.service.accountservice.AccountService;
import com.akman.springbootdemo.service.creditservice.CreditService;
import com.akman.springbootdemo.service.customerservice.CustomerService;
import com.akman.springbootdemo.service.transactionservice.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.WebApplicationInitializer;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;

@EnableBatchProcessing
@SpringBootApplication(scanBasePackageClasses = {
        DefaultBatchConfigurer.class,
        PackageMarker.class,
        com.akman.springbootdemo.PackageMarker.class})
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.akman.springbootdemo.repository"}
)
@EntityScan(basePackages = {"com.akman.springbootdemo.model"})
@RequiredArgsConstructor
public class SpringBootDemoApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(SpringBootDemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootDemoApplication.class);
    }

    private final ObjectMapper objectMapper;

    @PostConstruct
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Transactional
    @Bean
    public CommandLineRunner userAndCustomerUpdate(UserRepository userRepository, CustomerService customerService, AccountService accountService,
                                                   TransactionService transactionService, CreditService creditService) {
        return (args) -> {
            log.info("CommandLineRuner!");
            userRepository.save(User.builder().username("yasin").password("akman").build());

            Long firstCustomerResponseId = customerService.saveCustomer(CustomerRequest.builder().name("yasin").lastName("akman").address("Istanbul")
                    .dayOfBirth(LocalDate.of(2020, 3, 12)).rating(2).build()).getId();
            Long secondCustomerResponseId = customerService.saveCustomer(CustomerRequest.builder().name("talha").lastName("yasin").address("Bursa")
                    .dayOfBirth(LocalDate.of(2020, 3, 12)).rating(2).build()).getId();

            Long firstCustomerFirstAccountId = accountService.saveAccount(AccountRequest.builder().customer(firstCustomerResponseId)
                    .no(12345L).balance(BigDecimal.valueOf(50000)).currency(Currency.TRY).build()).getId();
            Long secondCustomerFirstAccountId = accountService.saveAccount(AccountRequest.builder().customer(secondCustomerResponseId)
                    .no(54321L).balance(BigDecimal.valueOf(50000)).currency(Currency.TRY).build()).getId();
            Long firstCustomerThirdAccountId = accountService.saveAccount(AccountRequest.builder().customer(firstCustomerResponseId)
                    .no(98765L).currency(Currency.TRY).build()).getId();
            Long firstCustomerFirstCreditId = creditService.saveCredit(CreditRequest.builder().customer(firstCustomerResponseId)
                    .creditAmount(BigDecimal.valueOf(1000)).currency(Currency.TRY).term(1L).build()).getId();

            transactionService.saveTransaction(TransactionRequest.builder().fromAccount(firstCustomerFirstAccountId).toAccount(secondCustomerFirstAccountId)
                    .currency(Currency.TRY).total(BigDecimal.valueOf(1000)).isCreditPayment(false).build());
            transactionService.saveTransaction(TransactionRequest.builder().fromAccount(secondCustomerFirstAccountId).toAccount(firstCustomerFirstAccountId)
                    .currency(Currency.TRY).total(BigDecimal.valueOf(500)).isCreditPayment(false).build());
            transactionService.saveTransaction(TransactionRequest.builder().fromAccount(firstCustomerFirstAccountId).toCredit(firstCustomerFirstCreditId)
                    .currency(Currency.TRY).total(BigDecimal.valueOf(500)).isCreditPayment(true).build());
        };
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        log.info("After run!");
    }
}