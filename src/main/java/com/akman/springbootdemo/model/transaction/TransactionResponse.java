package com.akman.springbootdemo.model.transaction;

import com.akman.springbootdemo.model.account.AccountResponse;
import com.akman.springbootdemo.model.credit.CreditResponse;
import com.akman.springbootdemo.model.enums.Currency;
import com.akman.springbootdemo.request_response.ResponseMapper;
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
public class TransactionResponse {

    @ApiModelProperty(notes = "id")
    private Long id;

    @ApiModelProperty(notes = "from account")
    private AccountResponse fromAccount;

    @ApiModelProperty(notes = "to account")
    private AccountResponse toAccount;

    @ApiModelProperty(notes = "to credit")
    private CreditResponse toCredit;

    @ApiModelProperty(notes = "total")
    private BigDecimal total;

    @ApiModelProperty(notes = "currency")
    private Currency currency;

    @ApiModelProperty(notes = "is credit payment")
    private boolean isCreditPayment;

    @ApiModelProperty(notes = "transaction time")
    private LocalDateTime transactionTime;

    public static TransactionResponse fromTransaction(Transaction transaction) {
        return ResponseMapper.MAPPER.convertTransactionToTransactionResponse(transaction);
    }
}