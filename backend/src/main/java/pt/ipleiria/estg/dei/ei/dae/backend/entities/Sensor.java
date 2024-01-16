package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Table(name = "sensor"
        , uniqueConstraints = @UniqueConstraint(columnNames = {"id", "idSensor"})
)
@NamedQueries({
        @NamedQuery(
                name = "getAllSensor",
                query = "SELECT s FROM Sensor s ORDER BY s.id" // JPQL
        )
})
@Entity
public class Sensor {

    // id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    // id_sensor -> o mesmo sensor vai ser utilizado para diferentes encomendas
    // e quando o sensor mudar de encomenda, vai registar novas observacoes
    // que terao de ter outra associaçao
    private long idSensor;
    // tipo (temperatura, humidade, etc)
    private String tipo;
    // unidade (ºC, %, etc)
    private String unidade;
    //0 = disponível, 1 = ocupado (esta associado a uma encomenda), 2 = sensorProduto (sem estado)
    private long estado;

    // o sensor vai ter uma lista de observaçoes numa relaçao de one to many
    @OneToMany(mappedBy = "sensor", fetch = FetchType.EAGER)
    private List<Observacao> observacoes;

    // relação com embalagem de many to many e vice versa
    @ManyToMany(mappedBy = "sensores", fetch = FetchType.EAGER)
    private List<Embalagem> embalagens;
    // relação de oneToMany com Observação e ManyToOne ao contrario

    public Sensor() {
        this.observacoes = new ArrayList<>();
        this.embalagens = new ArrayList<>();
    }

    public Sensor(long idSensor, String tipo, String unidade) {
        this.idSensor = idSensor;
        this.tipo = tipo;
        this.unidade = unidade;
        this.estado = 0;
        this.observacoes = new ArrayList<>();
        this.embalagens = new ArrayList<>();
    }


    public List<Embalagem> getEmbalagens() {
        return embalagens;
    }

    public void setEmbalagens(List<Embalagem> embalagens) {
        this.embalagens = embalagens;
    }

    public List<Observacao> getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(List<Observacao> observacoes) {
        this.observacoes = observacoes;
    }

    public void addObservacao(Observacao observacao) {
        observacoes.add(observacao);
    }

    public void addEmbalagem(Embalagem embalagem) {
        embalagens.add(embalagem);
    }

    public void removeEmbalagem(Embalagem embalagem) {
        embalagens.remove(embalagem);
    }

    public void removeObservacao(Observacao observacao) {
        observacoes.remove(observacao);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(long idSensor) {
        this.idSensor = idSensor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public long getEstado() {
        return estado;
    }

    public void setEstado(long estado) {
        this.estado = estado;
    }
}
