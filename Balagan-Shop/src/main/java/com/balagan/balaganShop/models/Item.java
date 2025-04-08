package com.balagan.balaganShop.models;

import jakarta.persistence.*;

import java.util.List;

@Table(name="item")
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    //@ManyToMany(mappedBy = "items", cascade = CascadeType.ALL)
    @JoinColumn(name = "id_composition") // Внешний ключ на CompositionOfApplication
    private CompositionOfApplication compositionOfApplication;

    private String name;
    private double value;
    @OneToOne
    @JoinColumn(name = "id_type")
    private Type type;

    @OneToOne
    @JoinColumn(name = "id_size")
    private Size size;

    private String photo;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Item() {
    }

    public Item(String name, double value, Type type, Size size, String photo) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.size = size;
        this.photo = photo;
    }
}
