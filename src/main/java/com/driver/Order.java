package com.driver;

import java.util.Arrays;
import java.util.List;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.id=id;
        setDeliveryTime(deliveryTime);
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = convertDeliveryTime(deliveryTime);
    }

    public static int convertDeliveryTime(String deliveryTime) {
        List<String> time= Arrays.asList(deliveryTime.split(":"));
        return Integer.parseInt(time.get(0))*60+Integer.parseInt(time.get(1));
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
        String HH=String.valueOf(this.deliveryTime/60);
        String MM=String.valueOf(this.deliveryTime%60);
        if(HH.length()==1)
            HH="0"+HH;
        if(MM.length()==1)
            MM="0"+MM;
        return HH+":"+MM;
    }

}
