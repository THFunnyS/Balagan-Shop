package com.balagan.balaganShop.models;

import jakarta.persistence.*;

@Table(name="items")
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name = "composition_id") // Внешний ключ в таблице items
    private CompositionOfApplication compositionOfApplication;
    private String name;
    private double value;
    @OneToOne
    @JoinColumn(name="id_type")
    private Type type;

    @OneToOne
    @JoinColumn(name="id_size")
    private Size size;

    private String photo;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CompositionOfApplication getCompositionOfApplication() {
        return compositionOfApplication;
    }

    public void setCompositionOfApplication(CompositionOfApplication compositionOfApplication) {
        this.compositionOfApplication = compositionOfApplication;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
