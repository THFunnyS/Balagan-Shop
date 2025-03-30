package com.balagan.balaganShop.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Table(name="composition_of_application")
@Entity
public class CompositionOfApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
     private int id;
    @OneToOne
    @JoinColumn(name="id_application")
    private Application application;
    private int amount_of_items;
    private double value_of_composition;

    @OneToMany(mappedBy = "compositionOfApplication", cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Application getApplication() { return application; }
    public void setApplication(Application application) { this.application = application; }

    public int getAmount_of_items() {
        return amount_of_items;
    }

    public void setAmount_of_items(int amount_of_items) {
        this.amount_of_items = amount_of_items;
    }

    public double getValue_of_composition() {
        return value_of_composition;
    }

    public void setValue_of_composition(double value_of_composition) {
        this.value_of_composition = value_of_composition;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
