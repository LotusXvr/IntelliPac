package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import pt.ipleiria.estg.dei.ei.dae.backend.entities.Sensor;

import java.util.ArrayList;
import java.util.List;

public class EmbalagemDeProdutoDTO {
    private Long id;

    private String material;
    private String tipoEmbalagem;
    private List<ProdutoDTO> produtoDTOS;

    private List<SensorDTO> sensorDTOS;

    public EmbalagemDeProdutoDTO() {
        this.produtoDTOS = new ArrayList<>();
        this.sensorDTOS = new ArrayList<>();
    }

    public EmbalagemDeProdutoDTO(Long id, String material, String tipoEmbalagem) {
        this.id = id;
        this.material = material;
        this.tipoEmbalagem = tipoEmbalagem;
        this.produtoDTOS = new ArrayList<>();
        this.sensorDTOS = new ArrayList<>();
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

    public String getTipoEmbalagem() {
        return tipoEmbalagem;
    }

    public void setTipoEmbalagem(String tipoEmbalagem) {
        this.tipoEmbalagem = tipoEmbalagem;
    }

    public List<ProdutoDTO> getProdutoDTOS() {
        return produtoDTOS;
    }

    public void setProdutoDTOS(List<ProdutoDTO> produtoDTOS) {
        this.produtoDTOS = produtoDTOS;
    }

    public List<SensorDTO> getSensorDTOS() {
        return sensorDTOS;
    }

    public void setSensorDTOS(List<SensorDTO> sensorDTOS) {
        this.sensorDTOS = sensorDTOS;
    }
}
