package com.order.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByProductNameAndProductPriceAndQuantity(String productName, BigDecimal productPrice, int quantity);

}


