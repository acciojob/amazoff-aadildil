package com.driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class Order {

    private String id;
    private String deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.id=id;
        setDeliveryTime(deliveryTime);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getDeliveryTime() {return deliveryTime;}


    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = convertDeliveryTime(deliveryTime);
    }

    public static String convertDeliveryTime(String deliveryTime) {
        List<String> time= Arrays.asList(deliveryTime.split(":"));
        return String.valueOf( Integer.parseInt(time.get(0))*60+Integer.parseInt(time.get(1)));
    }

    public static String convertDeliveryTime(int deliveryTime)
    {
        String HH=String.valueOf(deliveryTime/60);
        String MM=String.valueOf(deliveryTime%60);
        if(HH.length()==1)
            HH="0"+HH;
        if(MM.length()==1)
            MM="0"+MM;
        return HH+":"+MM;
    }
    public  String convertDeliveryTime()
    {
        int dTime=Integer.parseInt(this.deliveryTime);
        String HH=String.valueOf(dTime/60);
        String MM=String.valueOf(dTime%60);
        if(HH.length()==1)
            HH="0"+HH;
        if(MM.length()==1)
            MM="0"+MM;
        return HH+":"+MM;
    }

}
