package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import java.io.Serializable;

public class ObservacaoDTO implements Serializable {

    private String valor;
    private Long sensorId;

    public ObservacaoDTO() {
    }

    public ObservacaoDTO(String valor, Long sensorId) {
        this.valor = valor;
        this.sensorId = sensorId;
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

}
