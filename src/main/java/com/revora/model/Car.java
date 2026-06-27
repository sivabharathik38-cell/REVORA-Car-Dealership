package com.revora.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private Long price; // e.g. 4000000 for ₹40,00,000

    private String priceText; // e.g. "₹40 Lakh" or "₹40,00,000"
    private String fuel;      // "Petrol", "Diesel", "Electric"
    private String mileage;   // "15 km/l"
    private String transmission; // "Automatic", "Manual"
    private String engine;    // "2.8L"
    private String power;     // "204 PS"
    private String seating;   // "5 Seater", "7 Seater"
    private String body;      // "SUV", "Sedan"
    private String boot;      // "296 Litres"
    private String safety;    // "★★★★★"

    @Column(length = 1000)
    private String img;

    @Column(length = 1000)
    private String img1;

    @Column(length = 1000)
    private String img2;

    @Column(length = 1000)
    private String img3;

    @Column(length = 1000)
    private String img4;

    public Car() {}

    public Car(String name, String brand, Long price, String priceText, String fuel, String mileage,
               String transmission, String engine, String power, String seating, String body,
               String boot, String safety, String img) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.priceText = priceText;
        this.fuel = fuel;
        this.mileage = mileage;
        this.transmission = transmission;
        this.engine = engine;
        this.power = power;
        this.seating = seating;
        this.body = body;
        this.boot = boot;
        this.safety = safety;
        this.img = img;
        this.img1 = img;
    }

    public Car(String name, String brand, Long price, String priceText, String fuel, String mileage,
               String transmission, String engine, String power, String seating, String body,
               String boot, String safety, String img, String img1, String img2, String img3, String img4) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.priceText = priceText;
        this.fuel = fuel;
        this.mileage = mileage;
        this.transmission = transmission;
        this.engine = engine;
        this.power = power;
        this.seating = seating;
        this.body = body;
        this.boot = boot;
        this.safety = safety;
        this.img = img;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.img4 = img4;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getPriceText() {
        return priceText;
    }

    public void setPriceText(String priceText) {
        this.priceText = priceText;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getSeating() {
        return seating;
    }

    public void setSeating(String seating) {
        this.seating = seating;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBoot() {
        return boot;
    }

    public void setBoot(String boot) {
        this.boot = boot;
    }

    public String getSafety() {
        return safety;
    }

    public void setSafety(String safety) {
        this.safety = safety;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
        if (this.img1 == null || this.img1.isEmpty()) {
            this.img1 = img;
        }
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getImg4() {
        return img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
    }
}
