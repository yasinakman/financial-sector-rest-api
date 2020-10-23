package com.akman.springbootdemo.service.accountservice;

import com.akman.springbootdemo.model.account.Account;
import com.akman.springbootdemo.model.account.AccountRequest;
import com.akman.springbootdemo.model.account.AccountResponse;
import com.akman.springbootdemo.repository.AccountRepository;
import com.akman.springbootdemo.repository.CustomerRepository;
import com.akman.springbootdemo.request_response.RequestMapper;
import com.akman.springbootdemo.request_response.ResponseMapper;
import com.akman.springbootdemo.rest_api.exceptions.BadRequestException;
import com.akman.springbootdemo.service.customerservice.CustomerService;
import com.akman.springbootdemo.utils.ErrorConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerService customerService;

    @Transactional
    @Override
    public AccountResponse saveAccount(AccountRequest accountRequest) {
        Account account = RequestMapper.MAPPER.convertAccountRequestToAccount(accountRequest);
        if (account.getId() != null && !accountRepository.existsById(account.getId())) {
            throw new BadRequestException(ErrorConstants.COULD_NOT_FOUND_ANY_ACCOUNT_ID_TO_UPDATE);
        }
        try {
            account.setCustomer(customerService.getById(account.getCustomer().getId()));
            return ResponseMapper.MAPPER.convertAccountToAccountResponse(accountRepository.save(account));
        } catch (Exception e) {
            throw new BadRequestException(ErrorConstants.ACCOUNT_SAVE_FAILED);
        }
    }

    @Override
    public List<AccountResponse> listAccountsByCustomer(Long customerId) {
        return ResponseMapper.MAPPER.convertAccountListToAccountResponseList(accountRepository.findAllByCustomer_IdOrderByCreatedDateTimeDesc(customerId));
    }

    @Override
    public Account getById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorConstants.ACCOUNT_NOT_FOUND));
    }
}