package com.akman.springbootdemo.rest_api.controller;

import com.akman.springbootdemo.model.account.AccountRequest;
import com.akman.springbootdemo.model.account.AccountResponse;
import com.akman.springbootdemo.service.accountservice.AccountService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
@Api(tags = {"Account"})
@SwaggerDefinition(tags = {
        @Tag(name = "Account", description = "Account Management")
})
public class AccountController {

    private final AccountService accountService;

    @ApiOperation(value = "save account")
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

    @RequestMapping(value = "/save-account", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<AccountResponse> saveAccount(@RequestBody AccountRequest accountRequest) {
        AccountResponse accountResponse = accountService.saveAccount(accountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountResponse);
    }

    @ApiOperation(value = "list accounts by customer")
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

    @GetMapping(value = "/list-accounts-by-customer")
    public ResponseEntity<List<AccountResponse>> listAccountsByCustomer(@RequestParam Long customerId) {
        List<AccountResponse> accountResponseList = accountService.listAccountsByCustomer(customerId);
        return ResponseEntity.ok(accountResponseList);
    }
}