package com.akman.springbootdemo.model.transaction;

import com.akman.springbootdemo.model.enums.Currency;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel
public class TransactionRequest {

    @NotNull
    @ApiModelProperty(notes = "from account id")
    private Long fromAccount;

    @ApiModelProperty(notes = "to account id")
    private Long toAccount;

    @ApiModelProperty(notes = "to credit")
    private Long toCredit;

    @ApiModelProperty(notes = "total")
    private BigDecimal total;

    @ApiModelProperty(notes = "currency")
    private Currency currency;

    @ApiModelProperty(notes = "is credit payment")
    private boolean isCreditPayment;
}