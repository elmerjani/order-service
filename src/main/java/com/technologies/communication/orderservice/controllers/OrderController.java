package com.technologies.communication.orderservice.controllers;

import com.technologies.communication.orderservice.models.Order;
import com.technologies.communication.orderservice.models.OrderEvent;
import com.technologies.communication.orderservice.models.OrderStatus;
import com.technologies.communication.orderservice.models.ProductNotFoundException;
import com.technologies.communication.orderservice.publishers.OrderPublisher;
import com.technologies.communication.orderservice.repositories.OrderRepository;
import org.springframework.grpc.sample.proto.ProductRequest;
import org.springframework.grpc.sample.proto.ProductServiceGrpc;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

/**
 * @author : El-Merjani Mohamed
 * Date : 2/26/2025
 */
@RestController
@RequestMapping(value = "/v1/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProductServiceGrpc.ProductServiceBlockingStub stub;
    private final OrderPublisher orderPublisher;

    public OrderController(OrderRepository orderRepository, ProductServiceGrpc.ProductServiceBlockingStub stub, OrderPublisher orderPublisher) {
        this.orderRepository = orderRepository;
        this.stub = stub;
        this.orderPublisher = orderPublisher;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody Order order) {
        Long productId = order.getProductId();
        int quantity = order.getQuantity();
        if (productId == null || quantity <= 0) {
            return ResponseEntity.badRequest().build();
        }
        try {
            var request = ProductRequest.newBuilder().setId(productId).build();
            var response = stub.getProductById(request);
            double price = response.getPrice() * quantity;
            order.setTotalPrice(price);
            order.setStatus(OrderStatus.CREATED);
            var createdOrder = orderRepository.save(order);
            var orderEvent = new OrderEvent(createdOrder.getId(), createdOrder.getProductId(), createdOrder.getQuantity());
            orderPublisher.publish(orderEvent);
            var uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdOrder.getId())
                    .toUri();
            return ResponseEntity.created(uri).build();
        } catch (Exception e) {
            throw new ProductNotFoundException();
        }

    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping(value = "/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        var order = orderRepository.findById(orderId).orElseThrow(ProductNotFoundException::new);
        return ResponseEntity.ok(order);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Void> handleProductNotFoundException(ProductNotFoundException e) {
        return ResponseEntity.notFound().build();
    }


}
