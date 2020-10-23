package com.akman.springbootdemo.rest_api.controller;

import com.akman.springbootdemo.model.account.AccountRequest;
import com.akman.springbootdemo.model.account.AccountResponse;
import com.akman.springbootdemo.model.credit.CreditRequest;
import com.akman.springbootdemo.model.credit.CreditResponse;
import com.akman.springbootdemo.service.accountservice.AccountService;
import com.akman.springbootdemo.service.creditservice.CreditService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/credit")
@Api(tags = {"Credit"})
@SwaggerDefinition(tags = {
        @Tag(name = "Credit", description = "Credit Management")
})
public class CreditController {

    private final CreditService creditService;

    @ApiOperation(value = "save credit")
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

    @RequestMapping(value = "/save-credit", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity saveCredit(@RequestBody CreditRequest creditRequest) {
        CreditResponse creditResponse = creditService.saveCredit(creditRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(creditResponse);
    }

    @ApiOperation(value = "list all credits by customer")
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

    @GetMapping(value = "/list-credits")
    public ResponseEntity listCredits(@RequestParam(required = false) Long customerId,
                                      @RequestParam boolean isExceeded) {
        List<CreditResponse> creditResponseList = creditService.listCreditsByCustomer(customerId, isExceeded);
        return ResponseEntity.ok(creditResponseList);
    }
}