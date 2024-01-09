package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ObservacaoDTO implements Serializable {

    private long id;
    private String timestamp;
    private String valor;
    private long sensorId;

    public ObservacaoDTO() {
    }

    public ObservacaoDTO(long id, String timestamp, String valor, long sensorId) {
        this.id = id;
        this.timestamp = timestamp;
        this.valor = valor;
        this.sensorId = sensorId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void String(String valor) {
        this.valor = valor;
    }

    public long getSensorId() {
        return sensorId;
    }

    public void setSensorId(long sensorId) {
        this.sensorId = sensorId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
