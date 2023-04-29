package com.driver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/add-order")
    public ResponseEntity<String> addOrder(@RequestBody Order order)throws Exception{

        boolean o=orderService.addOrders(order);
        if(!o)
            throw new Exception("Order ID already exist");
        return new ResponseEntity<>("New order added successfully", HttpStatus.CREATED);
    }

    @PostMapping("/add-partner/{partnerId}")
    public ResponseEntity<String> addPartner(@PathVariable String partnerId) throws Exception{

        boolean p=orderService.addPartner(partnerId);
        if(!p)
            throw  new Exception("Partner already exist");
        return new ResponseEntity<>("New delivery partner added successfully", HttpStatus.CREATED);
    }

    @PutMapping("/add-order-partner-pair")
    public ResponseEntity<String> addOrderPartnerPair(@RequestParam String orderId, @RequestParam String partnerId){

        try {
            orderService.addOrderPartnerPair(orderId,partnerId);
            return new ResponseEntity<>("New order-partner pair added successfully", HttpStatus.CREATED);
        }catch (Exception exception)
        {
            return new ResponseEntity<>("order-partner pair updation failed",HttpStatus.NOT_FOUND);
        }

        //This is basically assigning that order to that partnerId

    }

    @GetMapping("/get-order-by-id/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) throws Exception{


        //order should be returned with an orderId.
        Order order=orderService.getOrderByOrderId(orderId);
        if(order.equals(null))
            throw  new Exception("Order ID doesn't Exist");

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/get-partner-by-id/{partnerId}")
    public ResponseEntity<DeliveryPartner> getPartnerById(@PathVariable String partnerId) throws Exception {



        //deliveryPartner should contain the value given by partnerId
        DeliveryPartner deliveryPartner=orderService.getPartnerById(partnerId);
        if(deliveryPartner.equals(null))
            throw new Exception("Delivery Partner Doesn't Exist");


        return new ResponseEntity<>(deliveryPartner, HttpStatus.CREATED);
    }

    @GetMapping("/get-order-count-by-partner-id/{partnerId}")
    public ResponseEntity<Integer> getOrderCountByPartnerId(@PathVariable String partnerId) throws Exception{

        DeliveryPartner deliveryPartner=orderService.getPartnerById(partnerId);
        if(deliveryPartner.equals(null))
            throw new Exception("Delivery Partner Doesn't Exist");
        Integer orderCount = orderService.getOrderCountByPartnerId(partnerId);

        //orderCount should denote the orders given by a partner-id

        return new ResponseEntity<>(orderCount, HttpStatus.CREATED);
    }

    @GetMapping("/get-orders-by-partner-id/{partnerId}")
    public ResponseEntity<List<String>> getOrdersByPartnerId(@PathVariable String partnerId) throws Exception{


        DeliveryPartner deliveryPartner=orderService.getPartnerById(partnerId);
        if(deliveryPartner.equals(null))
            throw new Exception("Delivery Partner Doesn't Exist");

        List<String> orders = orderService.getOrderByPartnerId(partnerId);
        if(orders.size()==0)
            throw new Exception("No orders assigned to "+partnerId);

        //orders should contain a list of orders by PartnerId

        return new ResponseEntity<>(orders, HttpStatus.CREATED);
    }

    @GetMapping("/get-all-orders")
    public ResponseEntity<List<String>> getAllOrders() throws Exception {
        List<String> orders = orderService.getAllOrders();
        if(orders.size()==0)
            throw new Exception("No orders haa been requested");


        //Get all orders
        return new ResponseEntity<>(orders, HttpStatus.CREATED);
    }

    @GetMapping("/get-count-of-unassigned-orders")
    public ResponseEntity<Integer> getCountOfUnassignedOrders(){
        Integer countOfOrders = orderService.getCountOfUnassignedOrders();

        //Count of orders that have not been assigned to any DeliveryPartner

        return new ResponseEntity<>(countOfOrders, HttpStatus.CREATED);
    }

    @GetMapping("/get-count-of-orders-left-after-given-time/{partnerId}")
    public ResponseEntity<Integer> getOrdersLeftAfterGivenTimeByPartnerId(@PathVariable String time, @PathVariable String partnerId) throws Exception {

        DeliveryPartner deliveryPartner=orderService.getPartnerById(partnerId);
        if(deliveryPartner.equals(null))
            throw new Exception("Delivery Partner Doesn't Exist");

        Integer countOfOrders = orderService.getOrdersLeftAfterGivenTimeByPartnerId(time,partnerId);

        //countOfOrders that are left after a particular time of a DeliveryPartner

        return new ResponseEntity<>(countOfOrders, HttpStatus.CREATED);
    }

    @GetMapping("/get-last-delivery-time/{partnerId}")
    public ResponseEntity<String> getLastDeliveryTimeByPartnerId(@PathVariable String partnerId) throws Exception {
        DeliveryPartner deliveryPartner=orderService.getPartnerById(partnerId);
        if(deliveryPartner.equals(null))
            throw new Exception("Delivery Partner Doesn't Exist");
        if(orderService.getOrderCountByPartnerId(partnerId)==0)
            throw new Exception("No orders assigned to "+partnerId);

        String time = orderService.getLastDeliveryTimeByPartnerId(partnerId);

        //Return the time when that partnerId will deliver his last delivery order.

        return new ResponseEntity<>(time, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-partner-by-id/{partnerId}")
    public ResponseEntity<String> deletePartnerById(@PathVariable String partnerId) throws Exception {

        DeliveryPartner deliveryPartner=orderService.getPartnerById(partnerId);
        if(deliveryPartner.equals(null))
            throw new Exception("Delivery Partner Doesn't Exist");
        //Delete the partnerId
        orderService.deletePartnerById(partnerId);
        //And push all his assigned orders to unassigned orders.

        return new ResponseEntity<>(partnerId + " removed successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-order-by-id/{orderId}")
    public ResponseEntity<String> deleteOrderById(@PathVariable String orderId) throws Exception {

        Order order=orderService.getOrderByOrderId(orderId);
        if(order.equals(null))
            throw  new Exception("Order ID doesn't Exist");
        //Delete an order and also
        orderService.deleteOrderById(orderId);
        // remove it from the assigned order of that partnerId

        return new ResponseEntity<>(orderId + " removed successfully", HttpStatus.CREATED);
    }
}
