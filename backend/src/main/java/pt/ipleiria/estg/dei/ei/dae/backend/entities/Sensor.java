package pt.ipleiria.estg.dei.ei.dae.backend.entities;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;



@Table(name = "sensor"
        , uniqueConstraints = @UniqueConstraint(columnNames = {"id"})
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
    // timestamp
    // type (temperatura, humidade, etc)
    // value
    private String value;
    // unit (ºC, %, etc)
    // id_embalagem
    // id_encomenda

    // relação com embalagem de many to many e vice versa
    @ManyToMany(mappedBy = "sensores")
    private List<Embalagem> embalagens;
    // relação de oneToMany com Observação e ManyToOne ao contrario

    public Sensor() {
        this.embalagens = new ArrayList<>();
    }

    public Sensor(int id, String value) {
        this.id = id;
        this.value = value;
        this.embalagens = new ArrayList<>();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Embalagem> getEmbalagens() {
        return embalagens;
    }

    public void setEmbalagens(List<Embalagem> embalagens) {
        this.embalagens = embalagens;
    }
}
