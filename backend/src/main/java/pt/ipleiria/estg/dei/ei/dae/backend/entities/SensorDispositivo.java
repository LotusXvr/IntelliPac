package pt.ipleiria.estg.dei.ei.dae.backend.entities;


import jakarta.persistence.*;

@Entity
public class SensorDispositivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String tipoSensorDispositivo;
    private String dadosSensor;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    public SensorDispositivo() {
    }

    public SensorDispositivo(String tipoSensorDispositivo, String dadosSensor, Produto produto) {
        this.tipoSensorDispositivo = tipoSensorDispositivo;
        this.dadosSensor = dadosSensor;
        this.produto = produto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTipoSensorDispositivo() {
        return tipoSensorDispositivo;
    }

    public void setTipoSensorDispositivo(String tipoSensorDispositivo) {
        this.tipoSensorDispositivo = tipoSensorDispositivo;
    }

    public String getDadosSensor() {
        return dadosSensor;
    }

    public void setDadosSensor(String dadosSensor) {
        this.dadosSensor = dadosSensor;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
