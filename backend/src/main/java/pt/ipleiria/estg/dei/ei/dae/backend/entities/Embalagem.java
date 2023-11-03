package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;

@Entity
public class Embalagem {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;

    private String tipoEmbalagem;
    private String material;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produtoEmb;

    public Embalagem() {
    }

    public Embalagem(String tipoEmbalagem, String material, Produto produtoEmb) {
        this.tipoEmbalagem = tipoEmbalagem;
        this.material = material;
        this.produtoEmb = produtoEmb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoEmbalagem() {
        return tipoEmbalagem;
    }

    public void setTipoEmbalagem(String tipoEmbalagem) {
        this.tipoEmbalagem = tipoEmbalagem;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Produto getProdutoEmb() {
        return produtoEmb;
    }

    public void setProdutoEmb(Produto produtoEmb) {
        this.produtoEmb = produtoEmb;
    }
}
