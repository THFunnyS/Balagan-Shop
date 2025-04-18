package com.balagan.balaganShop.models;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
@Table(name = "orders")
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "application_id")
    private Application application;
    private int amount_of_items;
    private double value_of_order;

    /*@ManyToMany
    @JoinTable(
            name = "orders_item",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items;*/

    @Getter
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetails> orderDetails;


    public void setId(Integer id) { this.id = id; }

    public void setApplication(Application application) { this.application = application; }

    public void setAmount_of_items(int amount_of_items) {
        this.amount_of_items = amount_of_items;
    }

    public void setValue_of_order(double value_of_order) {
        this.value_of_order = value_of_order;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    /*public void setItems(List<Item> items) {
        this.items = items;
    } */
}
