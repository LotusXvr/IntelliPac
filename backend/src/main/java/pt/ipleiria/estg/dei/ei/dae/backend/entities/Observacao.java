package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@NamedQueries({
        @NamedQuery(
                name = "getAllObservacao",
                query = "SELECT o FROM Observacao o ORDER BY o.id" // JPQL
        ),
        @NamedQuery(
                name = "getObservacoesBySensor",
                query = "SELECT o FROM Observacao o WHERE o.sensor.id = :id ORDER BY o.timestamp DESC" // JPQL
        ),
})
@Entity
public class Observacao {
    @Id
    @GeneratedValue
    private Long id;

    // sensor associado á observação
    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    // timestamp
    @Column(nullable = false)
    private String timestamp;

    // o valor registado
    @Column(nullable = false)
    private String valor;


    public Observacao() {
    }

    public Observacao(String timestamp, String valor, Sensor sensor) {
        this.sensor = sensor;
        this.timestamp = timestamp;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
