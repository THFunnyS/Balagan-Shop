package com.balagan.balaganShop.models;

import jakarta.persistence.*;

@Table(name="type")
@Entity
public class Type { //drop и добавить просто стрингом
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
