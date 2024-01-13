package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import java.util.ArrayList;
import java.util.List;

public class EmbalagemDeTransporteDTO {
    private Long id;
    private Long idEncomenda; // Referência à Encomenda
    private String material;
    private List<EncomendaDTO> encomendas;

    private List<SensorDTO> sensorDTOS;

    public EmbalagemDeTransporteDTO() {
        this.encomendas = new ArrayList<>();
        this.sensorDTOS = new ArrayList<>();
    }

    public EmbalagemDeTransporteDTO(Long id, String material, List<EncomendaDTO> encomendas) {
        this.id = id;
        this.material = material;
        this.encomendas = encomendas;
        this.sensorDTOS = new ArrayList<>();
    }

    public EmbalagemDeTransporteDTO(Long id, Long idEncomenda) {
        this.id = id;
        this.idEncomenda = idEncomenda;
        this.encomendas = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdEncomenda() {
        return idEncomenda;
    }

    public void setIdEncomenda(Long idEncomenda) {
        this.idEncomenda = idEncomenda;
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

    public List<SensorDTO> getSensorDTOS() {
        return sensorDTOS;
    }

    public void setSensorDTOS(List<SensorDTO> sensorDTOS) {
        this.sensorDTOS = sensorDTOS;
    }
}
