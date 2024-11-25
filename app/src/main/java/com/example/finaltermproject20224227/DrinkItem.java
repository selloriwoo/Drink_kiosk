package com.example.finaltermproject20224227;

public class DrinkItem {
    private String name;
    private byte[] pic;
    private int kindId;
    private int price;
    private int quantity;

    public DrinkItem(String name, byte[] pic, int kindId, int price) {
        this.name = name;
        this.pic = pic;
        this.kindId = kindId;
        this.price = price;
        this.quantity = 1;
    }

    public String getName() {
        return name;
    }

    public byte[] getPic() {
        return pic;
    }

    public int getKindId() {
        return kindId;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {return quantity;}

    public void setQuantity(int quantity) {this.quantity = quantity;}
    @Override
    public String toString() {
        return "DrinkItem{" +
                "name='" + name + '\'' +
                ", kindId=" + kindId +
                ", price=" + price +
                '}';
    }
}
