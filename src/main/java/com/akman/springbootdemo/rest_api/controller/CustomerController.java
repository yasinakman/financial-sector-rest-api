package com.akman.springbootdemo.rest_api.controller;

import com.akman.springbootdemo.model.customer.CustomerRequest;
import com.akman.springbootdemo.model.customer.CustomerResponse;
import com.akman.springbootdemo.model.enums.AscOrDesc;
import com.akman.springbootdemo.model.enums.CustomerField;
import com.akman.springbootdemo.service.customerservice.CustomerService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
@Api(tags = {"Customer"})
@SwaggerDefinition(tags = {
        @Tag(name = "Customer", description = "Customer Management")
})
public class CustomerController {

    private final CustomerService customerService;

    @ApiOperation(value = "save customer")
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

    @RequestMapping(value = "/save-customer", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<CustomerResponse> saveCustomer(@RequestBody CustomerRequest customerRequest) {
        CustomerResponse customerResponse = customerService.saveCustomer(customerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerResponse);
    }

    @ApiOperation(value = "list customers")
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

    @GetMapping(value = "/list-customers")
    public ResponseEntity<List<CustomerResponse>> listCustomersAndSortBy(@RequestParam(required = false) List<String> nameList,
                                                                         @RequestParam(required = false) List<String> lastNameList,
                                                                         @RequestParam CustomerField sortBy,
                                                                         @RequestParam AscOrDesc ascOrDesc) {
        List<CustomerResponse> customerResponseList = customerService.listCustomers(nameList, lastNameList, sortBy, ascOrDesc);
        return ResponseEntity.ok(customerResponseList);
    }
}