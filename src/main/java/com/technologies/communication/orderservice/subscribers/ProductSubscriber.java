package com.technologies.communication.orderservice.subscribers;

import com.technologies.communication.orderservice.models.OrderStatus;
import com.technologies.communication.orderservice.models.ProductEvent;
import com.technologies.communication.orderservice.models.ProductStatus;
import com.technologies.communication.orderservice.repositories.OrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

/**
 * @author : El-Merjani Mohamed
 * Date : 2/27/2025
 */
@Configuration
public class ProductSubscriber {
    private final OrderRepository orderRepository;

    public ProductSubscriber(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Bean
    public Consumer<ProductEvent> subscriber() {
        return productEvent -> {
            System.out.println(productEvent);
            var orderOptional = orderRepository.findById(productEvent.getOrderId());
            if (productEvent.getStatus().equals(ProductStatus.AVAILABLE)) {
                orderOptional.ifPresent(order -> order.setStatus(OrderStatus.PROCESSING));
            }
            else{
                orderOptional.ifPresent(order -> order.setStatus(OrderStatus.FAILED));
            }
            orderOptional.ifPresent(orderRepository::save);
        };
    }
}
