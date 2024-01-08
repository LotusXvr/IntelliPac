package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;

import java.time.LocalDate;


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
    private LocalDate timestamp;

    // o valor registado
    @Column(nullable = false)
    private String valor;


    public Observacao() {
    }

    public Observacao(LocalDate timestamp, String valor, Sensor sensor) {
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

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }
}
