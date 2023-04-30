package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {

    private Map<String,Order> orderMaps;//all orders
    private Map<String,DeliveryPartner>  partnerMap;//all delivery partners
    private Map<String,String>  orderDeliveryPartnerMap;//orders assigned to delivery partners
    private Map<String, List<String>> partnerAllOrdersMap;//orders handled by a delivery partner


    public OrderRepository() {
        orderMaps = new HashMap<>();
        partnerMap = new HashMap<>();
        orderDeliveryPartnerMap = new HashMap<>();
        partnerAllOrdersMap = new HashMap<>();
    }

    public boolean addOrder(Order order) {
        if(orderMaps.containsKey(order.getId()))
            return false;
        orderMaps.put(order.getId(), order);
        return true;
    }

    public boolean addPartner(DeliveryPartner deliveryPartner) {
        if(partnerMap.containsKey(deliveryPartner.getId()))
            return false;
        partnerMap.put(deliveryPartner.getId(), deliveryPartner);
        partnerAllOrdersMap.put(deliveryPartner.getId(),new ArrayList<>());
        return true;
    }

    public Optional<Order> getOrderById(String orderId) {
        if(orderMaps.containsKey(orderId))
            return Optional.of(orderMaps.get(orderId));
        else
            return Optional.empty();
    }

    public Optional<DeliveryPartner> getPartnerById(String partnerId) {
        if(partnerMap.containsKey(partnerId))
            return Optional.of(partnerMap.get(partnerId));
        else
            return Optional.empty();
    }


    public void addOrderPartnerPair(String orderId, String partnerId) {

        orderDeliveryPartnerMap.put(orderId,partnerId);//updating all order-partner map
        List<String> list=partnerAllOrdersMap.get(partnerId);//getting all orders of a specific delivery partner
        list.add(orderId);
        partnerAllOrdersMap.put(partnerId,list);//updating delivery partners orders list
        DeliveryPartner deliveryPartner=partnerMap.get(partnerId);//getting partner
        deliveryPartner.setNumberOfOrders(list.size());//updating number of orders


    }

    public Integer getOrderCountByPartnerId(String partnerId) {
     return partnerMap.get(partnerId).getNumberOfOrders();

    }

    public List<String> getOrderByPartnerId(String partnerId) {
        return partnerAllOrdersMap.get(partnerId);
    }

    public List<String> getAllOrders() {
       return new ArrayList<>(orderMaps.keySet());
    }

    public Integer getCountOfUnassignedOrders() {
        int ordersAssigned=orderDeliveryPartnerMap.size();
        int totalOrders=orderMaps.size();
        return totalOrders-ordersAssigned;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {

        int count=0;
        int currentTime=Order.convertDeliveryTime(time);
        List<String> orderList=partnerAllOrdersMap.get(partnerId);
        for(String orderId:orderList)
        {
            Optional<Order> orderOptional=getOrderById(orderId);
            int orderTime=orderOptional.get().getDeliveryTime();
            if(orderTime>currentTime)
                count++;
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {

        List<String> orderList=partnerAllOrdersMap.get(partnerId);
        int time=Integer.MIN_VALUE;
        for(String orderId:orderList)
        {
            Order order=getOrderById(orderId).get();
            if(order.getDeliveryTime()>time)
                time=order.getDeliveryTime();
        }
        return Order.convertDeliveryTime(time);

    }

    public void deletePartnerById(String partnerId) {

        DeliveryPartner partner=partnerMap.get(partnerId);

        //removed from partner Map
        partnerMap.remove(partnerId);

        //removing all orders of partner from order-partner map
        if(partnerAllOrdersMap.containsKey(partnerId))
        {
            List<String> orderList=partnerAllOrdersMap.get(partnerId);
            for(String orderId:orderList)
            {
                orderDeliveryPartnerMap.remove(orderId);
            }

            //removing partner from partner all orders map
            partnerAllOrdersMap.remove(partnerId);
        }
        partner.setNumberOfOrders(0);
    }

    public void deleteOrderById(String orderId) {
        orderMaps.remove(orderId);
    }

    public String getPartnerByOrderId(String orderId) {
        if(orderDeliveryPartnerMap.containsKey(orderId))
            return orderDeliveryPartnerMap.get(orderId);
        return null;
    }

    public void deleteOrderPartnerPairRecords(String orderId, String partnerId) {
        if(orderDeliveryPartnerMap.containsKey(orderId)) {
            orderDeliveryPartnerMap.remove(orderId);//removed from order partner pair
            List<String> orderList = partnerAllOrdersMap.get(partnerId);
            orderList.remove(orderId);//removed from partner's order list
            partnerAllOrdersMap.put(partnerId, orderList);
            getPartnerById(partnerId).get().setNumberOfOrders(orderList.size());
        }
    }
}
