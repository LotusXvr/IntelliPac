package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ProdutoCatalogo extends Produto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "produtoCatalogo")
    private List<ProdutoFisico> produtos;

    public ProdutoCatalogo() {
        this.produtos = new ArrayList<>();
    }

    public ProdutoCatalogo(String nomeProduto, FabricanteDeProdutos fabricante) {
        super(nomeProduto, fabricante);
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

}
