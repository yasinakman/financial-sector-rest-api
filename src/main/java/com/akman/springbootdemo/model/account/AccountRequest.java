package com.akman.springbootdemo.model.account;

import com.akman.springbootdemo.model.enums.Currency;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel
public class AccountRequest {

    @ApiModelProperty(notes = "id")
    private Long id;

    @ApiModelProperty(notes = "customer id")
    private Long customer;

    @ApiModelProperty(notes = "no")
    private Long no;

    @ApiModelProperty(notes = "balance")
    private BigDecimal balance;

    @ApiModelProperty(notes = "currency")
    private Currency currency;
}