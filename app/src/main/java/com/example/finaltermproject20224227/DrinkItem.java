package com.example.finaltermproject20224227;

import java.io.Serializable;

public class DrinkItem implements Serializable {
    private int id;
    private String name;
    private String pic; // 파일 이름을 저장하기 위한 String 타입
    private int kindId;
    private int price;
    private int quantity;

    public DrinkItem(String name, String pic, int kindId, int price) {
        this.name = name;
        this.pic = pic;
        this.kindId = kindId;
        this.price = price;
        this.quantity = 1;
    }

    public String getName() {
        return name;
    }

    public String getPic() {
        return pic;
    }

    public int getKindId() {
        return kindId;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() { return  id;}

    public void setId(int id) {this.id = id;}
}
