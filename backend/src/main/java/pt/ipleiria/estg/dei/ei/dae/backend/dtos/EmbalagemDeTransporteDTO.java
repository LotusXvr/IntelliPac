package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import java.util.ArrayList;
import java.util.List;

public class EmbalagemDeTransporteDTO {
    private Long id;
    private String material;

    private long altura;
    private long largura;
    private long comprimento;
    private List<EncomendaDTO> encomendas;
    private List<SensorDTO> sensores;
    private int estado;

    public EmbalagemDeTransporteDTO() {
        this.encomendas = new ArrayList<>();
        this.sensores = new ArrayList<>();
    }

    public EmbalagemDeTransporteDTO(long id, String material, long altura, long largura, long comprimento, int estado) {
        this.id = id;
        this.material = material;
        this.altura = altura;
        this.largura = largura;
        this.comprimento = comprimento;
        this.estado = estado;
        this.encomendas = new ArrayList<>();
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

    public long getAltura() {
        return altura;
    }

    public void setAltura(long altura) {
        this.altura = altura;
    }

    public long getLargura() {
        return largura;
    }

    public void setLargura(long largura) {
        this.largura = largura;
    }

    public long getComprimento() {
        return comprimento;
    }

    public void setComprimento(long comprimento) {
        this.comprimento = comprimento;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
