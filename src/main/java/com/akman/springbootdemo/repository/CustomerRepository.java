package com.akman.springbootdemo.repository;

import com.akman.springbootdemo.model.customer.Customer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("select c from Customer c where (COALESCE(:nameList, nullif(1,1)) is null or c.name in :nameList) and" +
            "(COALESCE(:lastNameList, nullif(1,1)) is null or c.lastName in :lastNameList)")
    List<Customer> findAllByNameAndLastNameIn(@Param("nameList") List<String> nameList, @Param("lastNameList") List<String> lastNameList, Sort sort);
}