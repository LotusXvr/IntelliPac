package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllProductsFisico",
                query = "SELECT p FROM ProdutoFisico p ORDER BY p.nomeProduto"
        )
})
public class ProdutoFisico extends Produto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "produto_catalogo_id")
    private ProdutoCatalogo produtoCatalogo;

    @ManyToMany
    @JoinTable(
            name = "produto_embalagem",
            joinColumns = @JoinColumn(
                    name = "product_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "embalagem_id",
                    referencedColumnName = "id"
            )
    )
    private List<EmbalagemDeProduto> embalagemDeProdutos;

    @ManyToOne
    @JoinColumn(name = "encomenda_id")
    private Encomenda encomenda;

    public ProdutoFisico() {
        embalagemDeProdutos = new ArrayList<>();
    }

    public ProdutoFisico(String nomeProduto, FabricanteDeProdutos fabricante, ProdutoCatalogo produtoCatalogo, Encomenda encomenda) {
        super(nomeProduto, fabricante);
        this.produtoCatalogo = produtoCatalogo;
        this.embalagemDeProdutos = new ArrayList<>();
        this.encomenda = encomenda;
    }

    public ProdutoFisico(ProdutoCatalogo produtoCatalogo, Encomenda encomenda) {
        this.produtoCatalogo = produtoCatalogo;
        this.embalagemDeProdutos = new ArrayList<>();
        this.encomenda = encomenda;
    }


    public List<EmbalagemDeProduto> getEmbalagemDeProdutos() {
        return embalagemDeProdutos;
    }

    public void setEmbalagemDeProdutos(List<EmbalagemDeProduto> embalagemDeProdutos) {
        this.embalagemDeProdutos = embalagemDeProdutos;
    }

    public ProdutoCatalogo getProdutoCatalogo() {
        return produtoCatalogo;
    }

    public void setProdutoCatalogo(ProdutoCatalogo produtoCatalogo) {
        this.produtoCatalogo = produtoCatalogo;
    }

    public void addEmbalagem(EmbalagemDeProduto embalagemDeProduto) {
        this.embalagemDeProdutos.add(embalagemDeProduto);
    }
    public void removeEmbalagem(EmbalagemDeProduto embalagemDeProduto) {
        this.embalagemDeProdutos.remove(embalagemDeProduto);
    }

    public Encomenda getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(Encomenda encomenda) {
        this.encomenda = encomenda;
    }
}
