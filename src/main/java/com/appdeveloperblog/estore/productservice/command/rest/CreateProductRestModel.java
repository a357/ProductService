package com.appdeveloperblog.estore.productservice.command.rest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRestModel {
    @NotBlank(message = "title can't be empty")
    private String title;
    @Min(value = 1, message = "price can't be less than 1")
    private BigDecimal price;
    @Min(value = 1, message = "quantity can't be less than 1")
    @Max(value = 5, message = "quantity can't be more than 5")
    private Integer quantity;
}
