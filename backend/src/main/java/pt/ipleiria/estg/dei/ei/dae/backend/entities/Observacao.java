package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Observacao {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String valor;

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    public Observacao() {
    }

    public Observacao(String valor, Sensor sensor, Produto produtoFisico, Embalagem embalagem) {
        this.valor = valor;
        this.sensor = sensor;
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
}
