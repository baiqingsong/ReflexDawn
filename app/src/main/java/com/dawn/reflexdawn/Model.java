package com.dawn.reflexdawn;

/**
 * Created by 90449 on 2017/7/10.
 */

public class Model {
    private String name;
    private int num;
    private boolean isShow;
    private float price;

    private static Model model;

    public Model(String name, int num, boolean isShow, float price) {
        this.name = name;
        this.num = num;
        this.isShow = isShow;
        this.price = price;
    }
    public static Model getInstance(String name, int num, boolean isShow, float price){
        if(model == null)
            model = new Model(name, num, isShow, price);
        return model;
    }
}
