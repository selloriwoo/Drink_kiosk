package com.example.finaltermproject20224227;

public class DrinkItem {
    private String name;
    private byte[] pic;
    private int kindId;
    private int price;

    public DrinkItem(String name, byte[] pic, int kindId, int price) {
        this.name = name;
        this.pic = pic;
        this.kindId = kindId;
        this.price = price;
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

    @Override
    public String toString() {
        return "DrinkItem{" +
                "name='" + name + '\'' +
                ", kindId=" + kindId +
                ", price=" + price +
                '}';
    }
}
