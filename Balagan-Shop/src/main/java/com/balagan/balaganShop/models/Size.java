package com.balagan.balaganShop.models;

import jakarta.persistence.*;

@Table(name="size")
@Entity
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int size;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
