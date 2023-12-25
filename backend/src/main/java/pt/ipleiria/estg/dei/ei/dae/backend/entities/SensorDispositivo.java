package pt.ipleiria.estg.dei.ei.dae.backend.entities;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany(mappedBy = "sensores")
    private List<Embalagem> embalagens;

    public SensorDispositivo() {
        this.embalagens = new ArrayList<>();
    }

    public SensorDispositivo(String tipoSensorDispositivo, String dadosSensor, Produto produto) {
        this.tipoSensorDispositivo = tipoSensorDispositivo;
        this.dadosSensor = dadosSensor;
        this.produto = produto;
        this.embalagens = new ArrayList<>();
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

    public List<Embalagem> getEmbalagens() {
        return embalagens;
    }

    public void setEmbalagens(List<Embalagem> embalagens) {
        this.embalagens = embalagens;
    }
}
