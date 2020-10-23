package com.akman.springbootdemo.service.customerservice;

import com.akman.springbootdemo.model.customer.Customer;
import com.akman.springbootdemo.model.customer.CustomerRequest;
import com.akman.springbootdemo.model.customer.CustomerResponse;
import com.akman.springbootdemo.model.enums.AscOrDesc;
import com.akman.springbootdemo.model.enums.CustomerField;

import java.util.List;

public interface CustomerService {

    CustomerResponse saveCustomer(CustomerRequest customerRequest);

    List<CustomerResponse> listCustomers(List<String> nameList, List<String> lastNameList, CustomerField sortBy, AscOrDesc ascOrDesc);

    Customer getById(Long id);
}