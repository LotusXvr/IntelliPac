package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import java.io.Serializable;
import java.time.LocalDate;

public class ObservacaoDTO implements Serializable {

    private long id;
    private LocalDate timestamp;
    private String valor;
    private Long sensorId;

    public ObservacaoDTO() {
    }

    public ObservacaoDTO(long id, LocalDate timestamp, String valor, Long sensorId) {
        this.id = id;
        this.timestamp = timestamp;
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

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

}
