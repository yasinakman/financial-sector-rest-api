package com.akman.springbootdemo.model.customer;

import com.akman.springbootdemo.utils.LocalDateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel
public class CustomerRequest {

    @ApiModelProperty(notes = "id")
    private Long id;

    @ApiModelProperty(notes = "name")
    private String name;

    @ApiModelProperty(notes = "last name")
    private String lastName;

    @ApiModelProperty(notes = "day of birth in format: 'yyyy-MM-dd'")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dayOfBirth;

    @ApiModelProperty(notes = "address")
    private String address;

    @ApiModelProperty(notes = "rating")
    private int rating;
}