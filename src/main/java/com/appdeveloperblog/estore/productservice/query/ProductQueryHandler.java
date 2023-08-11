package com.appdeveloperblog.estore.productservice.query;

import com.appdeveloperblog.estore.productservice.core.entity.ProductEntity;
import com.appdeveloperblog.estore.productservice.core.repository.ProductRepository;
import com.appdeveloperblog.estore.productservice.query.rest.ProductRestModel;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class ProductQueryHandler {
    private final ProductRepository productRepository;

    public ProductQueryHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @QueryHandler
    public List<ProductRestModel> findProducts(FindProductQuery findProductQuery) {
        var productsRest = new LinkedList<ProductRestModel>();
        var storedProducts = productRepository.findAll();

        for (ProductEntity entity : storedProducts) {
            var product = new ProductRestModel();
            BeanUtils.copyProperties(entity, product);
            productsRest.add(product);
        }
        return productsRest;
    }

}
