package pt.ipleiria.estg.dei.ei.dae.backend.entities;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class FabricanteDeProdutos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeFabricante;

    @OneToMany(mappedBy = "fabricante", fetch = FetchType.EAGER)
    private List<Produto> produtos;

    public FabricanteDeProdutos() {
        produtos = new ArrayList<>();
    }

    public FabricanteDeProdutos(String nomeFabricante) {
        this.nomeFabricante = nomeFabricante;
        produtos = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeFabricante() {
        return nomeFabricante;
    }

    public void setNomeFabricante(String nomeFabricante) {
        this.nomeFabricante = nomeFabricante;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public void addProduto(Produto produto) {
        produtos.add(produto);
    }

    public void removeProduto(Produto produto) {
        produtos.remove(produto);
    }
}
