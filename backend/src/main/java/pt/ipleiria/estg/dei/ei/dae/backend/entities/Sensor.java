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
    // id_sensor
    private long idSensor;
    // timestamp
    // tipo (temperatura, humidade, etc)
    private String tipo;
    // valor
    // No value ainda não sei se é string ou int
    private String valor;
    // unidade (ºC, %, etc)
    private String unidade;
    // id_embalagem
    // id_encomenda

    // relação com embalagem de many to many e vice versa
    @ManyToMany(mappedBy = "sensores")
    private List<Embalagem> embalagens;
    // relação de oneToMany com Observação e ManyToOne ao contrario

    public Sensor() {
        this.embalagens = new ArrayList<>();
    }

    public Sensor(long idSensor, String tipo, String valor, String unidade) {
        this.idSensor = idSensor;
        this.tipo = tipo;
        this.valor = valor;
        this.unidade = unidade;
        this.embalagens = new ArrayList<>();
    }


    public List<Embalagem> getEmbalagens() {
        return embalagens;
    }

    public void setEmbalagens(List<Embalagem> embalagens) {
        this.embalagens = embalagens;
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

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
}
