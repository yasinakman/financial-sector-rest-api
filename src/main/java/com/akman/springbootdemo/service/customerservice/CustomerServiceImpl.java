package com.akman.springbootdemo.service.customerservice;

import com.akman.springbootdemo.model.customer.Customer;
import com.akman.springbootdemo.model.customer.CustomerRequest;
import com.akman.springbootdemo.model.customer.CustomerResponse;
import com.akman.springbootdemo.model.enums.AscOrDesc;
import com.akman.springbootdemo.model.enums.CustomerField;
import com.akman.springbootdemo.repository.CustomerRepository;
import com.akman.springbootdemo.request_response.RequestMapper;
import com.akman.springbootdemo.request_response.ResponseMapper;
import com.akman.springbootdemo.rest_api.exceptions.BadRequestException;
import com.akman.springbootdemo.utils.ErrorConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    @Override
    public CustomerResponse saveCustomer(CustomerRequest customerRequest) {
        if (customerRequest.getRating() < 1 || customerRequest.getRating() > 4) {
            throw new BadRequestException(ErrorConstants.RATING_CAN_NOT_BE_LESS_THAN_1_AND_GREATER_THAN_4);
        }
        Customer customer = RequestMapper.MAPPER.convertCustomerRequestToCustomer(customerRequest);
        if (customer.getId() != null && !customerRepository.existsById(customer.getId())) {
            throw new BadRequestException(ErrorConstants.CUSTOMER_SAVE_FAILED);
        }
        try {
            return ResponseMapper.MAPPER.convertCustomerToCustomerResponse(customerRepository.save(customer));
        } catch (Exception e) {
            throw new BadRequestException(ErrorConstants.CUSTOMER_SAVE_FAILED);
        }
    }

    @Override
    public List<CustomerResponse> listCustomers(List<String> nameList, List<String> lastNameList, CustomerField customerField, AscOrDesc ascOrDesc) {
        Sort orderBy = AscOrDesc.ASCENDING.equals(ascOrDesc) ? Sort.by(customerField.getValue()).ascending() : Sort.by(customerField.getValue()).descending();
        return ResponseMapper.MAPPER.convertCustomerListToCustomerResponseList(customerRepository.findAllByNameAndLastNameIn(nameList, lastNameList, orderBy));
    }

    @Override
    public Customer getById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorConstants.CUSTOMER_NOT_FOUND));
    }
}
