package com.akman.springbootdemo.model.credit;

import com.akman.springbootdemo.model.customer.CustomerResponse;
import com.akman.springbootdemo.model.enums.Currency;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel
public class CreditResponse {

    @ApiModelProperty(notes = "id")
    private Long id;

    @ApiModelProperty(notes = "customer")
    private CustomerResponse customer;

    @ApiModelProperty(notes = "credit amount")
    private BigDecimal creditAmount;

    @ApiModelProperty(notes = "remaining credit amount")
    private BigDecimal remainingCreditAmount;

    @ApiModelProperty(notes = "currency")
    private Currency currency;

    @ApiModelProperty(notes = "original term in days")
    private Long term;

    @ApiModelProperty(notes = "remaining term in days")
    private Long remainingTerm;

    @ApiModelProperty(notes = "created date time")
    private LocalDateTime createdDateTime;

    @ApiModelProperty(notes = "is active")
    private boolean isActive;

}