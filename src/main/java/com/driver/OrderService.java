package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@Service
public class OrderService {

    @Autowired
OrderRepository orderRepository;

    public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }

    public void addPartner(DeliveryPartner deliveryPartner) {
        orderRepository.addPartner(deliveryPartner);
    }



    public ResponseEntity<Order> getOrderById(String orderId) {
        Optional<Order> order=orderRepository.getOrderById(orderId);

        return new ResponseEntity<>(order.get(), HttpStatus.OK);

    }

    public ResponseEntity<String> addOrderPartnerPair(String orderId, String partnerId) {
        return orderRepository.addOrderPartnerPair(orderId,partnerId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return orderRepository.getPartnerById(partnerId);
    }

    public int getOrderCountByPartner(String partnerId) {
        DeliveryPartner partner=orderRepository.getPartnerById(partnerId);
        return partner.getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return orderRepository.getOrdersByPartnerId(partnerId);
    }

    public List<String> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public int getCountUnassignedOrders() {
        return orderRepository.getCountUnassignedOrders();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        return orderRepository.getOrdersLeftAfterGivenTimeByPartnerId(time,partnerId);
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        return orderRepository.getLastDeliveryTimeByPartnerId(partnerId);
    }

    public void deletePartnerById(String partnerId) {
        orderRepository.deletePartnerById(partnerId);
    }

    public void deleteOrderById(String orderId) {
        orderRepository.deleteOrderById(orderId);
    }
}
