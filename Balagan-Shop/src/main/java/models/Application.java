package models;

import jakarta.persistence.*;

@Table(name = "Applications")
@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String FIO;
    private int phone_number;
    private String telegram;

    @ManyToOne
    @JoinColumn(name="id_manager")
    private Manager manager;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFIO() {
        return FIO;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public int getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(int phone_number) {
        this.phone_number = phone_number;
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
}
