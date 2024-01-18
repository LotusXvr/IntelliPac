package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllProductsFisico",
                query = "SELECT p FROM ProdutoFisico p ORDER BY p.nomeProduto"
        ),
        @NamedQuery(
                name = "getAllProductsFisicoFromFabricante",
                query = "SELECT p FROM ProdutoFisico p WHERE p.fabricante.username = :username AND (p.encomenda.estado = 'TRANSPORTE' OR p.encomenda.estado = 'PROCESSAMENTO') ORDER BY p.nomeProduto"
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
    private List<EmbalagemDeProduto> embalagensDeProduto;

    @ManyToOne
    @JoinColumn(name = "encomenda_id")
    private Encomenda encomenda;

    public ProdutoFisico() {
        embalagensDeProduto = new ArrayList<>();
    }

    public ProdutoFisico(String nomeProduto, FabricanteDeProdutos fabricante, ProdutoCatalogo produtoCatalogo, Encomenda encomenda, long peso) {
        super(nomeProduto, fabricante, peso);
        this.produtoCatalogo = produtoCatalogo;
        this.embalagensDeProduto = new ArrayList<>();
        this.encomenda = encomenda;
    }

    public ProdutoFisico(ProdutoCatalogo produtoCatalogo, Encomenda encomenda) {
        this.produtoCatalogo = produtoCatalogo;
        this.embalagensDeProduto = new ArrayList<>();
        this.encomenda = encomenda;
    }


    public List<EmbalagemDeProduto> getEmbalagensDeProduto() {
        return embalagensDeProduto;
    }

    public void setEmbalagensDeProduto(List<EmbalagemDeProduto> embalagensDeProduto) {
        this.embalagensDeProduto = embalagensDeProduto;
    }

    public ProdutoCatalogo getProdutoCatalogo() {
        return produtoCatalogo;
    }

    public void setProdutoCatalogo(ProdutoCatalogo produtoCatalogo) {
        this.produtoCatalogo = produtoCatalogo;
    }

    public void addEmbalagem(EmbalagemDeProduto embalagemDeProduto) {
        this.embalagensDeProduto.add(embalagemDeProduto);
    }

    public void removeEmbalagem(EmbalagemDeProduto embalagemDeProduto) {
        this.embalagensDeProduto.remove(embalagemDeProduto);
    }

    public Encomenda getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(Encomenda encomenda) {
        this.encomenda = encomenda;
    }
}
