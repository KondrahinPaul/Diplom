package model;

import java.io.Serializable;

public class Contact implements Serializable {

    private int id;
    private String name;
    private String address;
    private String phone;
    private String age;
    private String shopLike;
    private String preferense;


    public Contact() {
    }

    public Contact(int id, String age, String name, String address, String phone, String preferense, String shopLike) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.preferense = preferense;
        this.age = age;
        this.shopLike=shopLike;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAge() {return age;}

    public void setAge(String age) {
        this.age = age;
    }

    public String getShopLike() {
        return shopLike;
    }

    public void setShopLike(String shopLike) {
        this.shopLike = shopLike;
    }

    public String getPreferense() {
        return preferense;
    }

    public void setPreferense(String preferense) {
        this.preferense = preferense;
    }

    public void setValues(Contact other){
        setName(other.getName());
        setAddress(other.getAddress());
        setPhone(other.getPhone());
        setAge(other.getAge());
        setPreferense(other.getPreferense());
        setShopLike(other.getShopLike());
    }

    @Override
    public String toString() {
        return "Contact {" +
                "id= " + id +
                ", age= " + age+'\''+
                ", name= " + name + '\''+
                ", adress= " + address+ '\'' +
                ", phones= " + phone+'\''+
                ", preferense= "+preferense +'\''+
                ", shopLike= " + shopLike+'\''+
                '}';
    }
}
