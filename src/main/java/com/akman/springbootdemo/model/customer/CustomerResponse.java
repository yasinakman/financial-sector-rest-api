package com.akman.springbootdemo.model.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {

    @ApiModelProperty(notes = "id")
    private Long id;

    @ApiModelProperty(notes = "name")
    private String name;

    @ApiModelProperty(notes = "last name")
    private String lastName;

    @ApiModelProperty(notes = "day of birth in format: 'yyyy-MM-dd'")
    private LocalDate dayOfBirth;

    @ApiModelProperty(notes = "address")
    private String address;

    @ApiModelProperty(notes = "rating")
    private int rating;
}