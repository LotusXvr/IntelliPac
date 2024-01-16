package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllTipoEmbalagem",
                query = "SELECT p FROM TipoEmbalagemProduto p ORDER BY p.tipoEmbalagem"
        )
})
public class TipoEmbalagemProduto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Min(value = 1, message = "A coluna deve ter apenas os valores 1, 2 ou 3")
    @Max(value = 3, message = "A coluna deve ter apenas os valores 1, 2 ou 3")
    private long tipoEmbalagem;

    private String material;
    @ManyToMany(mappedBy = "embalagensACriar", fetch = FetchType.EAGER)
    private List<ProdutoCatalogo> produtoCatalogo;

    @ManyToMany
    @JoinTable(
            name = "tipoEmbalagemProduto_TipoSensor",
            joinColumns = @JoinColumn(
                    name = "tipoEmbalagem_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "tipoSensor_id",
                    referencedColumnName = "id"
            )
    )
    private List<TipoSensor> tipoSensor;
    public TipoEmbalagemProduto(long tipoEmbalagem, String material) {
        this.tipoEmbalagem = tipoEmbalagem;
        this.material = material;
        this.tipoSensor = new ArrayList<>();
    }

    public TipoEmbalagemProduto() {
        this.tipoSensor = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTipoEmbalagem() {
        return tipoEmbalagem;
    }

    public void setTipoEmbalagem(long tipoEmbalagem) {
        this.tipoEmbalagem = tipoEmbalagem;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public List<ProdutoCatalogo> getProdutoCatalogo() {
        return produtoCatalogo;
    }

    public void setProdutoCatalogo(List<ProdutoCatalogo> produtoCatalogo) {
        this.produtoCatalogo = produtoCatalogo;
    }

    public List<TipoSensor> getTipoSensor() {
        return tipoSensor;
    }

    public void setTipoSensor(List<TipoSensor> tipoSensor) {
        this.tipoSensor = tipoSensor;
    }

    public void addTipoSensor(TipoSensor sensor) {
        this.tipoSensor.add(sensor);
    }

    public void removeTipoSensor(TipoSensor sensor) {
        this.tipoSensor.remove(sensor);
    }
}
