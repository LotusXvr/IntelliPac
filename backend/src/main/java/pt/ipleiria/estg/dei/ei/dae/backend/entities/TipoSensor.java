package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllTipoSensor",
                query = "SELECT s FROM TipoSensor s ORDER BY s.tipo"
        )
})
public class TipoSensor implements Serializable {

    //0 = disponível, 1 = ocupado (esta associado a uma encomenda), 2 = sensorProduto (sem estado)
    // Como o tipo sensor só pode ser associado a embalagens de produto, este valor vai ser sempre 2
    private final long estado = 2;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String tipo;
    // unidade (ºC, %, etc)
    private String unidade;
    @ManyToMany(mappedBy = "tipoSensor", fetch = FetchType.EAGER)
    private List<TipoEmbalagemProduto> tipoEmbalagemProdutos;

    public TipoSensor() {
        this.tipoEmbalagemProdutos = new ArrayList<>();
    }

    public TipoSensor(String tipo, String unidade) {
        this.tipo = tipo;
        this.unidade = unidade;
        this.tipoEmbalagemProdutos = new ArrayList<>();
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<TipoEmbalagemProduto> getTipoEmbalagemProdutos() {
        return tipoEmbalagemProdutos;
    }

    public void setTipoEmbalagemProdutos(List<TipoEmbalagemProduto> tipoEmbalagemProdutos) {
        this.tipoEmbalagemProdutos = tipoEmbalagemProdutos;
    }

    public long getEstado() {
        return estado;
    }

}
