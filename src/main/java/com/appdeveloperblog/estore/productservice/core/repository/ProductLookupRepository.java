package com.appdeveloperblog.estore.productservice.core.repository;

import com.appdeveloperblog.estore.productservice.core.entity.ProductLookupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLookupRepository extends JpaRepository<ProductLookupEntity, String> {
    ProductLookupEntity findByProductIdOrTitle(String productId, String title);
}
