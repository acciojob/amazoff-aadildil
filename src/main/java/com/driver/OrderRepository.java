package com.driver;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {

    private Map<String,Order> orderMaps=new HashMap<>();//all orders
    private Map<String,DeliveryPartner>  partnerMap=new HashMap<>();//all delivery partners
    private Map<String,String>  orderDeliveryPartnerMap=new HashMap<>();//orders assigned to delivery partners
    private Map<String, List<String>> partnerAllOrdersMap=new HashMap<>();//orders handled by a delivery partner


    public void addOrder(Order order) {

        orderMaps.put(order.getId(),order);
    }

    public void addPartner(DeliveryPartner deliveryPartner) {
        partnerMap.put(deliveryPartner.getId(), deliveryPartner);



        List<String> orders=new ArrayList<>();
        partnerAllOrdersMap.put(deliveryPartner.getId(), orders);
    }

    public Optional<Order> getOrderById(String orderId) {
        if(orderMaps.containsKey(orderId))
            return Optional.of(orderMaps.get(orderId));
        else
            return Optional.empty();
    }

    public ResponseEntity<String> addOrderPartnerPair(String orderId, String partnerId) {
        if(orderMaps.containsKey(orderId)&&partnerMap.containsKey(partnerId))
        {
            orderDeliveryPartnerMap.put(orderId,partnerId);

            List<String> orders=partnerAllOrdersMap.get(partnerId);
            if(!orders.contains(orderId)) {
                orders.add(orderId);
                partnerAllOrdersMap.put(partnerId, orders);
                DeliveryPartner deliveryPartner = partnerMap.get(partnerId);
                deliveryPartner.setNumberOfOrders(deliveryPartner.getNumberOfOrders() + 1);
            }

            return new ResponseEntity<>("Success", HttpStatus.CREATED);
        }
        else
            return new ResponseEntity<>("partnerId or OrderId doesn't exist",HttpStatus.NOT_FOUND);

    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return partnerMap.get(partnerId);
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        if(partnerAllOrdersMap.containsKey(partnerId))
            return partnerAllOrdersMap.get(partnerId);
        return new ArrayList<>();
    }

    public List<String> getAllOrders() {
        List<String> orders=new ArrayList<>();
        if(!orderMaps.isEmpty())
        {
            for(String orderId:orderMaps.keySet())
            {
                orders.add(orderId);
            }
        }
        return orders;
    }

    public int getCountUnassignedOrders() {
        int assigned=orderDeliveryPartnerMap.size();
        int total=orderMaps.size();
        return total-assigned;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        int numericalTime = Integer.parseInt(time.substring(0,2))*60 + Integer.parseInt(time.substring(3,5));
        int count = 0;
        for(String orderId : partnerAllOrdersMap.get(partnerId)){
            if(Integer.parseInt(orderMaps.get(orderId).getDeliveryTime())>numericalTime){
                count++;
            }
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        int latestTime = 0;
        if(partnerAllOrdersMap.containsKey(partnerId)&&partnerAllOrdersMap.get(partnerId).size()>0){
            for(String currOrderId : partnerAllOrdersMap.get(partnerId)){
                if(Integer.parseInt(orderMaps.get(currOrderId).getDeliveryTime())>latestTime){
                    latestTime = Integer.parseInt(orderMaps.get(currOrderId).getDeliveryTime());
                }
            }
        }

        int hours = latestTime/60;
        int minute = latestTime%60;

        String strhours = Integer.toString(hours);
        if(strhours.length()==1){
            strhours = "0"+strhours;
        }

        String minutes = Integer.toString(minute);
        if(minutes.length()==1){
            minutes = "0" + minutes;
        }
        return strhours + ":" + minutes;

    }

    public void deletePartnerById(String partnerId) {

        for(String orderId:orderDeliveryPartnerMap.keySet())
        {
            if(orderDeliveryPartnerMap.get(orderId).equals(partnerId))
            {
                orderDeliveryPartnerMap.remove(orderId);
            }
        }
        if(partnerMap.containsKey(partnerId))
        partnerMap.remove(partnerId);
        if(partnerAllOrdersMap.containsKey(partnerId))
        partnerAllOrdersMap.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
        if(orderDeliveryPartnerMap.containsKey(orderId))
        {
            String deliveryPartner=orderDeliveryPartnerMap.get(orderId);
            orderDeliveryPartnerMap.remove(orderId);

            List<String> orders=partnerAllOrdersMap.get(deliveryPartner);
            if(orders.contains(orderId))
            orders.remove(orderId);
            partnerAllOrdersMap.put(deliveryPartner,orders);

            DeliveryPartner dp=partnerMap.get(deliveryPartner);
            dp.setNumberOfOrders(dp.getNumberOfOrders()-1);


        }
        if(orderMaps.containsKey(orderId))
            orderMaps.remove(orderId);


    }
}
