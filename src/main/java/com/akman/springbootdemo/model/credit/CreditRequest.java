package com.akman.springbootdemo.model.credit;

import com.akman.springbootdemo.model.customer.Customer;
import com.akman.springbootdemo.model.customer.CustomerResponse;
import com.akman.springbootdemo.model.enums.Currency;
import com.akman.springbootdemo.rest_api.exceptions.BadRequestException;
import com.akman.springbootdemo.utils.ErrorConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel
public class CreditRequest {

    @ApiModelProperty(notes = "id")
    private Long id;

    @ApiModelProperty(notes = "customer")
    private Long customer;

    @ApiModelProperty(notes = "credit amount")
    private BigDecimal creditAmount;

    @ApiModelProperty(notes = "currency")
    private Currency currency;

    @ApiModelProperty(notes = "original term in days")
    private Long term;

    public void validations(Customer customer) {
        if (customer.getCreditList() != null) {
            if (customer.getCreditList().stream().anyMatch(Credit::isActive)) {
                throw new BadRequestException(ErrorConstants.CUSTOMER_HAS_AN_ACTIVE_CREDIT);
            }
        } else if (customer.getAccountList() != null) {
            boolean isValid = customer.getAccountList().stream().anyMatch(account -> account.getCurrency().equals(currency));
            if (!isValid) {
                throw new BadRequestException(ErrorConstants.CUSTOMER_HAS_NOT_ANY_ACCOUNT_WITH_COMPATIBLE_CURRENCY_TO_CREDIT_CURRENCY);
            }
        }
    }
}