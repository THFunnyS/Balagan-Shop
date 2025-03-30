package com.balagan.balaganShop.models;

import jakarta.persistence.*;

@Table(name = "applications")
@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String FIO;
    private String phoneNumber;
    private String telegram;

    @ManyToOne
    @JoinColumn(name="id_manager")
    private Manager manager;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFIO() {
        return FIO;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
