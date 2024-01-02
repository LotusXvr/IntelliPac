package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import java.io.Serializable;

public class ObservacaoDTO implements Serializable {

    private String valor;
    private Long sensorId;
    private Long produtoFisicoId;
    private Long embalagemId;

    public ObservacaoDTO() {
    }

    public ObservacaoDTO(String valor, Long sensorId, Long produtoFisicoId, Long embalagemId) {
        this.valor = valor;
        this.sensorId = sensorId;
        this.produtoFisicoId = produtoFisicoId;
        this.embalagemId = embalagemId;
    }

    public String getValor() {
        return valor;
    }

    public void String(String valor) {
        this.valor = valor;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        sensorId = sensorId;
    }

    public Long getProdutoFisicoId() {
        return produtoFisicoId;
    }

    public void setProdutoFisicoId(Long produtoFisicoId) {
        this.produtoFisicoId = produtoFisicoId;
    }

    public Long getEmbalagemId() {
        return embalagemId;
    }

    public void setEmbalagemId(Long embalagemId) {
        this.embalagemId = embalagemId;
    }
}
