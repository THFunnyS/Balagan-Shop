package com.balagan.balaganShop.models;

import jakarta.persistence.*;
import lombok.Getter;
@Getter
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

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    //@JoinColumn(name = "order_id")
    private Order order;

    /*@ManyToOne
    @JoinColumn(name = "id_manager")
    private Manager manager;*/

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    /*public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }*/

    public void setOrder(Order order) {
        this.order = order;
    }

    public Application(){}

    public Application(String FIO, String phoneNumber, String telegram){
        this.FIO = FIO;
        this.phoneNumber = phoneNumber;
        this.telegram = telegram;
    }
}
