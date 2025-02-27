package com.technologies.communication.orderservice.repositories;

import com.technologies.communication.orderservice.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : El-Merjani Mohamed
 * Date : 2/26/2025
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
}
