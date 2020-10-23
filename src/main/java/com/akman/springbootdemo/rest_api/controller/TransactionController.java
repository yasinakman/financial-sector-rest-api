package com.akman.springbootdemo.rest_api.controller;

import com.akman.springbootdemo.model.enums.AscOrDesc;
import com.akman.springbootdemo.model.enums.TransactionField;
import com.akman.springbootdemo.model.enums.TransactionType;
import com.akman.springbootdemo.model.transaction.TransactionRequest;
import com.akman.springbootdemo.model.transaction.TransactionResponse;
import com.akman.springbootdemo.service.transactionservice.TransactionService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
@Api(tags = {"Transaction"})
@SwaggerDefinition(tags = {
        @Tag(name = "Transaction", description = "Transaction Management")
})
public class TransactionController {

    private final TransactionService transactionService;

    @ApiOperation(value = "save transaction")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 202, message = "Accepted"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 422, message = "Unprocessable entity"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })

    @RequestMapping(value = "/save-transaction-or-credit-payment", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity saveTransaction(@RequestBody @Valid TransactionRequest transactionRequest) {
        TransactionResponse transactionResponse = transactionService.saveTransaction(transactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponse);
    }

    @ApiOperation(value = "list transactions by customer account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 202, message = "Accepted"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 422, message = "Unprocessable entity"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })

    @GetMapping(value = "/list-transactions-by-account")
    public ResponseEntity listTransactionsByAccount(@RequestParam Long accountId,
                                                    @RequestParam(defaultValue = "IN_OUT") TransactionType transactionType) {
        List<TransactionResponse> transactionResponseList =
                transactionType.equals(TransactionType.IN_OUT) ? transactionService.listTransactionsByAccount(accountId)
                        : transactionType.equals(TransactionType.INCOMING) ? transactionService.listIncomingTransactionsByAccount(accountId)
                        : transactionType.equals(TransactionType.OUTGOING) ? transactionService.listOutgoingTransactionsByAccount(accountId) : null;
        return ResponseEntity.ok(transactionResponseList);
    }

    @ApiOperation(value = "list transactions by customer credit")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 202, message = "Accepted"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 422, message = "Unprocessable entity"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })

    @GetMapping(value = "/list-transactions-by-credit")
    public ResponseEntity listTransactionsByAccount(@RequestParam Long creditId) {
        List<TransactionResponse> transactionResponseList = transactionService.listTransactionsByCredit(creditId);
        return ResponseEntity.ok(transactionResponseList);
    }

    @ApiOperation(value = "get transactions by booking date, account, customer name and get the result sortable and pageable")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 202, message = "Accepted"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 422, message = "Unprocessable entity"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })

    @GetMapping(value = "/list-all-transactions")
    public ResponseEntity listAllTransactions(@RequestParam(required = false) String name,
                                              @RequestParam(required = false) Long accountId,
                                              @RequestParam(defaultValue = "TRANSACTION_TIME") TransactionField sortBy,
                                              @RequestParam AscOrDesc ascOrDesc,
                                              @RequestParam(required = false) LocalDateTime startDateTime,
                                              @RequestParam(required = false) LocalDateTime endDateTime,
                                              @RequestParam boolean isPage,
                                              @RequestParam(required = false) Integer pageNo,
                                              @RequestParam(required = false) Integer pageSize) {
        if (isPage) {
            Page<TransactionResponse> transactionResponseList = transactionService
                    .listAllTransactionsPageable(name, accountId, sortBy, ascOrDesc, startDateTime, endDateTime, pageNo, pageSize);
            return ResponseEntity.ok(transactionResponseList);
        } else {
            List<TransactionResponse> transactionResponseList = transactionService.listAllTransactions(name, accountId, sortBy, ascOrDesc, startDateTime, endDateTime);
            return ResponseEntity.ok(transactionResponseList);
        }
    }
}