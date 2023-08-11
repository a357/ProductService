package com.appdeveloperblog.estore.productservice.query;

import com.appdeveloperblog.estore.core.events.ProductReservationCanceledEvent;
import com.appdeveloperblog.estore.core.events.ProductReservedEvents;
import com.appdeveloperblog.estore.productservice.core.entity.ProductEntity;
import com.appdeveloperblog.estore.productservice.core.events.ProductCreatedEvent;
import com.appdeveloperblog.estore.productservice.core.repository.ProductRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import static com.appdeveloperblog.estore.productservice.core.procgroups.Groups.PRODUCT_GROUP;

@Component
@ProcessingGroup(PRODUCT_GROUP)
public class ProductEventHandler {
    private final ProductRepository productRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(ProductEventHandler.class);

    public ProductEventHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception exception) throws Exception {
        //TODO propagate exception 2  rethrow exception
        throw exception;

    }

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalArgumentException exception) {

    }

    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        ProductEntity entity = new ProductEntity();
        BeanUtils.copyProperties(productCreatedEvent, entity);
        productRepository.save(entity);
    }

    @EventHandler
    public void on(ProductReservedEvents productReservedEvents) {
        var entity = productRepository.findByProductId(productReservedEvents.getProductId());

        LOGGER.info(String.format("ProductReservedEvents current product quantity:%s", entity.getQuantity()));

        entity.setQuantity(entity.getQuantity() - productReservedEvents.getQuantity());
        productRepository.save(entity);

        LOGGER.info(String.format("ProductReservedEvents new product quantity:%s", entity.getQuantity()));
        LOGGER.info(String.format("Product reserved event is called for product:%s and order:%s",
                productReservedEvents.getProductId(), productReservedEvents.getOrderId()));
    }

    @EventHandler
    public void on(ProductReservationCanceledEvent productReservationCanceledEvent) {
        var entity = productRepository.findByProductId(productReservationCanceledEvent.getProductId());
        LOGGER.info(String.format("ProductReservationCanceledEvent: current product quantity", entity.getQuantity()));
        entity.setQuantity(entity.getQuantity() + productReservationCanceledEvent.getQuantity());
        productRepository.save(entity);
        LOGGER.info(String.format("ProductReservationCanceledEvent: new product quantity", entity.getQuantity()));

        LOGGER.info(String.format("Product reservation event was canceled for product:%s and order:%s",
                productReservationCanceledEvent.getProductId(),
                productReservationCanceledEvent.getOrderId()));
    }
}
