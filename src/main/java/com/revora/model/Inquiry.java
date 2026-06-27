package com.revora.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inquiries")
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Optional relation if logged in
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    // Optional relation if inquiring about a specific car
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", nullable = true)
    private Car car;

    private String name;  // Guest name or fallback
    private String email; // Guest email or fallback

    @Column(nullable = false, length = 2000)
    private String message;

    @Column(nullable = false)
    private String inquiryDate; // e.g. "10 June 2026"

    @Column(nullable = false)
    private String status = "Pending"; // "Pending", "Replied"

    @Column(length = 2000)
    private String reply;

    public Inquiry() {}

    public Inquiry(User user, Car car, String name, String email, String message, String inquiryDate, String status, String reply) {
        this.user = user;
        this.car = car;
        this.name = name;
        this.email = email;
        this.message = message;
        this.inquiryDate = inquiryDate;
        this.status = status;
        this.reply = reply;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInquiryDate() {
        return inquiryDate;
    }

    public void setInquiryDate(String inquiryDate) {
        this.inquiryDate = inquiryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
