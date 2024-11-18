package com.order.management.repository;

import com.order.management.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByProductNameAndProductPriceAndQuantity(String productName, BigDecimal productPrice, int quantity);

}


