package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllProductsCatalogo",
                query = "SELECT p FROM ProdutoCatalogo p ORDER BY p.nomeProduto"
        )
})
public class ProdutoCatalogo extends Produto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "produtoCatalogo")
    private List<ProdutoFisico> produtos;

    private long peso;
    public ProdutoCatalogo() {
        this.produtos = new ArrayList<>();
    }

    public ProdutoCatalogo(String nomeProduto, FabricanteDeProdutos fabricante, long peso) {
        super(nomeProduto, fabricante);
        this.peso = peso;
        this.produtos = new ArrayList<>();
    }

    public List<ProdutoFisico> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoFisico> produtos) {
        this.produtos = produtos;
    }

    public void addProduto(ProdutoFisico produto) {
        this.produtos.add(produto);
    }

    public void removeProduto(ProdutoFisico produto) {
        this.produtos.remove(produto);
    }

    public long getPeso() {
        return peso;
    }

    public void setPeso(long peso) {
        this.peso = peso;
    }
}
