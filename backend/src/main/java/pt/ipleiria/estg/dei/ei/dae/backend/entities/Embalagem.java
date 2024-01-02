package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Embalagem {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    private String material;

    @ManyToMany
    @JoinTable(
            name = "embalagem_sensor",
            joinColumns = @JoinColumn(
                    name = "embalagem_id",
                    referencedColumnName = "id"
            ),
            // adicionalmente terá um data de instalação
            // para verificar se o sensor está atualmente em x embalagem
            // ou se já está desatualizado
            inverseJoinColumns = @JoinColumn(
                    name = "sensor_id",
                    referencedColumnName = "id"
            )
    )
    private List<SensorDispositivo> sensores;

    public Embalagem() {
        this.sensores = new ArrayList<>();
    }

    public Embalagem(String material) {
        this.material = material;
        this.sensores = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public List<SensorDispositivo> getSensores() {
        return sensores;
    }

    public void setSensores(List<SensorDispositivo> sensores) {
        this.sensores = sensores;
    }


}
