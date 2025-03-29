package models;

import jakarta.persistence.*;

@Table(name="Composition_Of_Application")
@Entity
public class CompositionOfApplication {
    @OneToOne
    @JoinColumn(name="id_composition")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Application application;
    private int amount_of_items;
    private double value_of_composition;
    @ManyToMany
    @JoinColumn(name="id_item")
    private Item item;

    public Application getId() {
        return application;
    }

    public void setId(Application application) {
        this.application = application;
    }

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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
