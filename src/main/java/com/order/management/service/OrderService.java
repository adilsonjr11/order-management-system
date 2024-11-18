package com.order.management.service;

import com.order.management.model.Order;
import com.order.management.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Cacheable("orders")
    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackGetAllOrders")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @CacheEvict(value = "orders", allEntries = true)
    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackGetAllOrders")
    public Order createOrder(Order order) {
        if (isDuplicate(order)) {
            throw new IllegalArgumentException("Pedido duplicado");
        }

        BigDecimal totalPrice = order.getProductPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
        order.setTotalPrice(totalPrice);
        order.setStatus("PENDING");

        Order savedOrder = orderRepository.save(order);
        //Publica o pedido na fila
        rabbitTemplate.convertAndSend("orderQueue", savedOrder);
        return savedOrder;
    }

    private boolean isDuplicate(Order order) {
        List<Order> existingOrders = orderRepository.findByProductNameAndProductPriceAndQuantity(
                order.getProductName(), order.getProductPrice(), order.getQuantity());
        return !existingOrders.isEmpty();
    }

    @CacheEvict(value = "orders", allEntries = true)
    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackGetAllOrders")
    public Order updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public List<Order> fallbackGetAllOrders(Throwable t) {
        return List.of();  // Retorna uma lista vazia ou uma resposta padrão
    }

    public Order fallbackCreateOrder(Order order, Throwable t) {
        return new Order();  // Retorna um pedido padrão ou uma resposta alternativa
    }

    public Order fallbackUpdateOrderStatus(Long id, String status, Throwable t) {
        return new Order();  // Retorna um pedido padrão ou uma resposta alternativa
    }

}
