package com.customerproduct.customerproductapi.repository;

import com.customerproduct.customerproductapi.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

}
