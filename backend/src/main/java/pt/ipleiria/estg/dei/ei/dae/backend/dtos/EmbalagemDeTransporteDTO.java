package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import java.util.ArrayList;
import java.util.List;

public class EmbalagemDeTransporteDTO {
    private Long id;
    private String material;
    private List<EncomendaDTO> encomendas;

    private List<SensorDTO> sensores;

    public EmbalagemDeTransporteDTO() {
        this.encomendas = new ArrayList<>();
        this.sensores = new ArrayList<>();
    }

    public EmbalagemDeTransporteDTO(long id, String material) {
        this.id = id;
        this.material = material;
        this.encomendas = new ArrayList<>();
        this.sensores = new ArrayList<>();
    }

    public EmbalagemDeTransporteDTO(Long id, String material, List<EncomendaDTO> encomendas) {
        this.id = id;
        this.material = material;
        this.encomendas = encomendas;
        this.sensores = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<EncomendaDTO> getEncomendas() {
        return encomendas;
    }

    public void setEncomendas(List<EncomendaDTO> encomendas) {
        this.encomendas = encomendas;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public List<SensorDTO> getSensores() {
        return sensores;
    }

    public void setSensores(List<SensorDTO> sensores) {
        this.sensores = sensores;
    }
}
