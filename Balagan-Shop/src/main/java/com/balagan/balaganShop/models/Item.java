package com.balagan.balaganShop.models;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
@Table(name="item")
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /*@OneToOne
    //@ManyToMany(mappedBy = "items", cascade = CascadeType.ALL)
    @JoinColumn(name = "id_composition") // Внешний ключ на CompositionOfApplication
    private CompositionOfApplication compositionOfApplication;*/

    private String name;
    private double value;
    @OneToOne
    @JoinColumn(name = "type_id")
    private Type type;

    @OneToOne
    @JoinColumn(name = "size_id")
    private Size size;

    private String photo;


    public void setId(Integer id) {
        this.id = id;
    }

    /*public CompositionOfApplication getCompositionOfApplication() {
        return compositionOfApplication;
    }

    public void setCompositionOfApplication(CompositionOfApplication compositionOfApplication) {
        this.compositionOfApplication = compositionOfApplication;
    }*/

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setSize(Size size) {
        this.size = size;
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
