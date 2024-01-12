package pt.ipleiria.estg.dei.ei.dae.backend.dtos;


import jakarta.persistence.OneToMany;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.EmbalagemSensor;

import java.util.ArrayList;
import java.util.List;

public class EmbalagemDTO {
    private long id;
    private String material;

    private List<SensorDTO> sensores;

    public EmbalagemDTO() {
        this.sensores = new ArrayList<>();
    }

    public EmbalagemDTO(long id, String material) {
        this.id = id;
        this.material = material;
        this.sensores = new ArrayList<>();
    }

    public EmbalagemDTO(long id, String material, List<SensorDTO> sensores) {
        this.id = id;
        this.material = material;
        this.sensores = sensores;
    }

    public List<SensorDTO> getSensores() {
        return sensores;
    }

    public void setSensores(List<SensorDTO> sensores) {
        this.sensores = sensores;
    }

    public void addSensor(SensorDTO sensor) {
        this.sensores.add(sensor);
    }

    public void removeSensor(SensorDTO sensor) {
        this.sensores.remove(sensor);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }


}
