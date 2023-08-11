package com.appdeveloperblog.estore.productservice.command;

import com.appdeveloperblog.estore.productservice.core.entity.ProductLookupEntity;
import com.appdeveloperblog.estore.productservice.core.events.ProductCreatedEvent;
import com.appdeveloperblog.estore.productservice.core.repository.ProductLookupRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import static com.appdeveloperblog.estore.productservice.core.procgroups.Groups.PRODUCT_GROUP;

@Component
@ProcessingGroup(PRODUCT_GROUP)
public class ProductLookupEventsHandler {
    private final ProductLookupRepository productLookupRepository;

    public ProductLookupEventsHandler(ProductLookupRepository productLookupRepository) {
        this.productLookupRepository = productLookupRepository;
    }

    @EventHandler
    public void on(ProductCreatedEvent event) {
        ProductLookupEntity entity = new ProductLookupEntity(event.getProductId(), event.getTitle());

        productLookupRepository.save(entity);
    }
}
