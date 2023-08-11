package com.appdeveloperblog.estore.productservice.command.rest;

import com.appdeveloperblog.estore.productservice.command.CreateProductCommand;
import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductsCommandController {

    private final Environment env;
    private final CommandGateway commandGateway;

    @Autowired
    public ProductsCommandController(Environment env, CommandGateway commandGateway) {
        this.env = env;
        this.commandGateway = commandGateway;
    }

    /**
     * TODO 1 first option valid object spring boot starter validation @Valid
    */
    @PostMapping
    public String createProduct(@Valid @RequestBody CreateProductRestModel createProductRestModel) {

        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .productId(UUID.randomUUID().toString())
                .title(createProductRestModel.getTitle())
                .price(createProductRestModel.getPrice())
                .quantity(createProductRestModel.getQuantity())
                .build();

        String result;

        result = commandGateway.sendAndWait(createProductCommand);

        //TODO ProductServiceErrorHandling -> @ControllerAdvice
//        try {
//            result = commandGateway.sendAndWait(createProductCommand);
//        } catch (Exception e) {
//            result = e.getLocalizedMessage();
//        }

        return result;
    }
}
