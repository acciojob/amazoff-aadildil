package com.driver;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    OrderRepository orderRepository;

    public boolean addOrders(Order order) {
        return orderRepository.addOrder(order);
    }

    public boolean addPartner(String partnerId) {
        DeliveryPartner deliveryPartner=new DeliveryPartner(partnerId);
        return orderRepository.addPartner(deliveryPartner);
    }

    public boolean addOrderPartnerPair(String orderId, String partnerId) throws Exception{
        Optional<Order> order=orderRepository.getOrderById(orderId);
        Optional<DeliveryPartner> deliveryPartner=orderRepository.getPartnerById(partnerId);
        if(order.isEmpty())
            throw new Exception("order doesn't exist");
        if(deliveryPartner.isEmpty())
            throw new Exception("Delivery partner doesn't exist");

       orderRepository.addOrderPartnerPair(orderId,partnerId);

        return true;
    }

    public Order getOrderByOrderId(String orderId) {
       Optional<Order> order= orderRepository.getOrderById(orderId);
       if(order.isEmpty())
           return null;
       return order.get();
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        Optional<DeliveryPartner> deliveryPartner=orderRepository.getPartnerById(partnerId);
        if(deliveryPartner.isEmpty())
            return null;
        return deliveryPartner.get();
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        return orderRepository.getOrderCountByPartnerId(partnerId);
    }

    public List<String> getOrderByPartnerId(String partnerId) {
        return orderRepository.getOrderByPartnerId(partnerId);
    }

    public List<String> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public Integer getCountOfUnassignedOrders() {
        return orderRepository.getCountOfUnassignedOrders();
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
        String partnerId=orderRepository.getPartnerbyOrderId(orderId);
        orderRepository.deleteOrderPartnerPairRecords(orderId,partnerId);

    }
}
