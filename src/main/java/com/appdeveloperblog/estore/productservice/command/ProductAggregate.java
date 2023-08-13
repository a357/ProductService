package com.appdeveloperblog.estore.productservice.command;

import com.appdeveloperblog.estore.core.commands.CancelProductReservationCommand;
import com.appdeveloperblog.estore.core.commands.ReserveProductCommand;
import com.appdeveloperblog.estore.core.events.ProductReservationCanceledEvent;
import com.appdeveloperblog.estore.core.events.ProductReservedEvents;
import com.appdeveloperblog.estore.productservice.core.events.ProductCreatedEvent;
import org.apache.logging.log4j.util.Strings;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

import static com.appdeveloperblog.estore.productservice.ProductServiceApplication.PRODUCT_SNAPSHOT_TRIGGER_DEFINITION;

@Aggregate(snapshotTriggerDefinition = PRODUCT_SNAPSHOT_TRIGGER_DEFINITION)
public class ProductAggregate {
    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;

    public ProductAggregate() {
    }


    /**
     * TODO 2 second option valid object @CommandHandler
     */
    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) {
        if (Strings.isBlank(createProductCommand.getTitle())) {
            throw new IllegalArgumentException("title can't be empty");
        }

        if (createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("price can't be less or equal 0");
        }

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
        BeanUtils.copyProperties(createProductCommand, productCreatedEvent);

        AggregateLifecycle.apply(productCreatedEvent);
    }

    @CommandHandler
    public void handle(ReserveProductCommand reserveProductCommand) {
        if (reserveProductCommand.getQuantity() > this.quantity) {
            throw new IllegalArgumentException("Insufficient number of items in stock");
        }

        ProductReservedEvents productReservedEvents = ProductReservedEvents.builder()
                .productId(reserveProductCommand.getProductId())
                .orderId(reserveProductCommand.getOrderId())
                .userId(reserveProductCommand.getUserId())
                .quantity(reserveProductCommand.getQuantity())
                .build();

        AggregateLifecycle.apply(productReservedEvents);
    }

    @CommandHandler
    public void handle(CancelProductReservationCommand cancelProductReservationCommand) {
        var productReservationCanceledEvent = ProductReservationCanceledEvent.builder()
                .productId(cancelProductReservationCommand.getProductId())
                .orderId(cancelProductReservationCommand.getOrderId())
                .userId(cancelProductReservationCommand.getUserId())
                .quantity(cancelProductReservationCommand.getQuantity())
                .reason(cancelProductReservationCommand.getReason())
                .build();

        AggregateLifecycle.apply(productReservationCanceledEvent);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        this.productId = productCreatedEvent.getProductId();
        this.title = productCreatedEvent.getTitle();
        this.price = productCreatedEvent.getPrice();
        this.quantity = productCreatedEvent.getQuantity();
    }

    @EventSourcingHandler
    public void on(ProductReservedEvents productReservedEvents) {
        this.quantity -= productReservedEvents.getQuantity();
    }

    @EventSourcingHandler
    public void on(ProductReservationCanceledEvent productReservationCanceledEvent) {
        this.quantity += productReservationCanceledEvent.getQuantity();
    }
}
