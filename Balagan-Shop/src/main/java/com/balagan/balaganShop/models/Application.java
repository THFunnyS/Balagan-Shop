package com.balagan.balaganShop.models;

import jakarta.persistence.*;

@Table(name = "application")
@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "fio")
    private String FIO;
    @Column(name = "phonenumber")
    private String phoneNumber;
    private String telegram;

    @ManyToOne
    @JoinColumn(name = "id_manager")
    private Manager manager;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Application(){}

    public Application(String FIO, String phoneNumber, String telegram){
        this.FIO = FIO;
        this.phoneNumber = phoneNumber;
        this.telegram = telegram;
    }
}
