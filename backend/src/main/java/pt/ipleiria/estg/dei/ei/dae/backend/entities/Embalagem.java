package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Embalagem {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private long id;
    private String material;

    @OneToMany(mappedBy = "embalagem")
    private List<EmbalagemSensor> sensores;

    public Embalagem() {
        this.sensores = new ArrayList<>();
    }

    public Embalagem(String material) {
        this.material = material;
        this.sensores = new ArrayList<>();
    }

    public List<EmbalagemSensor> getSensores() {
        return sensores;
    }

    public void setSensores(List<EmbalagemSensor> sensores) {
        this.sensores = sensores;
    }

    public void addSensor(EmbalagemSensor sensor) {
        this.sensores.add(sensor);
    }

    public void removeSensor(EmbalagemSensor sensor) {
        this.sensores.remove(sensor);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }




}
