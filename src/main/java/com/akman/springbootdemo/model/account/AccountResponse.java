package com.akman.springbootdemo.model.account;

import com.akman.springbootdemo.model.customer.CustomerResponse;
import com.akman.springbootdemo.model.enums.Currency;
import com.akman.springbootdemo.model.transaction.Transaction;
import com.akman.springbootdemo.model.transaction.TransactionResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel
public class AccountResponse {

    @ApiModelProperty(notes = "id")
    private Long id;

    @ApiModelProperty(notes = "customer")
    private CustomerResponse customer ;

    @ApiModelProperty(notes = "no")
    private Long no;

    @ApiModelProperty(notes = "balance")
    private BigDecimal balance;

    @ApiModelProperty(notes = "currency")
    private Currency currency;

    @ApiModelProperty(notes = "created date time")
    private LocalDateTime createdDateTime;
}