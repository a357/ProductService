package com.appdeveloperblog.estore.productservice.core.repository;

import com.appdeveloperblog.estore.productservice.core.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {

    ProductEntity findByProductId(String productId);

    ProductEntity findByProductIdOrTitle(String productId, String title);

}
