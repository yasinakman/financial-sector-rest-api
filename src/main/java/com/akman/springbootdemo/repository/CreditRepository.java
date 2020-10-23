package com.akman.springbootdemo.repository;

import com.akman.springbootdemo.model.credit.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CreditRepository extends JpaRepository<Credit, Long> {
    @Query("select c from Credit c where :customerId is null or c.customer.id = :customerId")
    List<Credit> findAllByCustomer_IdOrderByCreatedDateTimeDesc(@Param("customerId") Long customerId);

    List<Credit> findAllByIsActive(boolean isActive);
}