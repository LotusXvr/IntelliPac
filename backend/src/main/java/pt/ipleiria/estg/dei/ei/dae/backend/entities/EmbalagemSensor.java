package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;

@Entity
public class EmbalagemSensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne
    @JoinColumn(name = "embalagem_id")
    private Embalagem embalagem;

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @Column(name = "data_instalacao")
    private String dataInstalacao;

    public EmbalagemSensor() {
    }

    public EmbalagemSensor(Embalagem embalagem, Sensor sensor, String dataInstalacao) {
        this.embalagem = embalagem;
        this.sensor = sensor;
        this.dataInstalacao = dataInstalacao;
    }

    public Embalagem getEmbalagem() {
        return embalagem;
    }

    public void setEmbalagem(Embalagem embalagem) {
        this.embalagem = embalagem;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public String getDataInstalacao() {
        return dataInstalacao;
    }

    public void setDataInstalacao(String dataInstalacao) {
        this.dataInstalacao = dataInstalacao;
    }

}
