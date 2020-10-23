package com.akman.springbootdemo.service.creditservice;

import com.akman.springbootdemo.model.credit.Credit;
import com.akman.springbootdemo.model.credit.CreditRequest;
import com.akman.springbootdemo.model.credit.CreditResponse;
import com.akman.springbootdemo.model.customer.Customer;
import com.akman.springbootdemo.repository.CreditRepository;
import com.akman.springbootdemo.repository.CustomerRepository;
import com.akman.springbootdemo.request_response.RequestMapper;
import com.akman.springbootdemo.request_response.ResponseMapper;
import com.akman.springbootdemo.rest_api.exceptions.BadRequestException;
import com.akman.springbootdemo.service.customerservice.CustomerService;
import com.akman.springbootdemo.utils.ErrorConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;
    private final CustomerService customerService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public CreditResponse saveCredit(CreditRequest creditRequest) {
        Credit credit = RequestMapper.MAPPER.convertCreditRequestToCredit(creditRequest);
        if (credit.getId() != null && !creditRepository.existsById(credit.getId())) {
            throw new BadRequestException(ErrorConstants.COULD_NOT_FOUND_ANY_CREDIT_TO_UPDATE);
        }
        Customer customer = customerService.getById(credit.getCustomer().getId());
        creditRequest.validations(customer);
        credit.setCustomer(customer);
        credit.setActive(true);
        try {
            return ResponseMapper.MAPPER.convertCreditToCreditResponse(creditRepository.save(credit));
        } catch (Exception e) {
            throw new BadRequestException(ErrorConstants.CREDIT_SAVE_FAILED);
        }
    }

    @Override
    public List<CreditResponse> listCreditsByCustomer(Long customerId, boolean isExceeded) {
        List<Credit> credits = creditRepository.findAllByCustomer_IdOrderByCreatedDateTimeDesc(customerId);
        if (isExceeded) {
            credits = credits.stream()
                    .filter(credit -> credit.getTerm() - ChronoUnit.DAYS.between(credit.getCreatedDateTime(), LocalDateTime.now()) <= 0)
                    .collect(Collectors.toList());
        }
        return ResponseMapper.MAPPER.convertCreditListToCreditResponseList(credits);
    }

    @Override
    public Credit getById(Long id) {
        return creditRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorConstants.CREDIT_NOT_FOUND));
    }

    @Override
    public void updateCreditsAndCustomerRatingsPerJob() {
        creditRepository.findAllByIsActive(true);
    }
}