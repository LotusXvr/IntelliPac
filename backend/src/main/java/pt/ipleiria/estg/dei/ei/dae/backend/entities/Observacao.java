package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@NamedQueries({
        @NamedQuery(
                name = "getAllObservacao",
                query = "SELECT o FROM Observacao o ORDER BY o.id" // JPQL
        )
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    // o valor registado
    @Column(nullable = false)
    private String valor;


    public Observacao() {
    }

    public Observacao(LocalDateTime timestamp, String valor, Sensor sensor) {
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

    public void String(String valor) {
        this.valor = valor;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
