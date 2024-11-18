package com.order.management.service;

import com.order.management.model.Order;
import com.order.management.repository.OrderRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderMessageListener {

    @Autowired
    private OrderRepository orderRepository;

    @RabbitListener(queues = "orderQueue")
    public void handleOrder(Order order) {
        // Lógica para processar o pedido
        order.setStatus("PROCESSED");
        orderRepository.save(order);
    }
}
