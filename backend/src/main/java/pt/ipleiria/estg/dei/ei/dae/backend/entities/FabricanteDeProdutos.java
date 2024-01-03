package pt.ipleiria.estg.dei.ei.dae.backend.entities;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class FabricanteDeProdutos extends User implements Serializable {
    @OneToMany(mappedBy = "fabricante", fetch = FetchType.EAGER)
    private List<Produto> produtos;

    public FabricanteDeProdutos() {
        this.produtos = new ArrayList<>();
    }

    public FabricanteDeProdutos(String username, String password, String nome, String email) {
        super(username, password, nome, email);
        this.produtos = new ArrayList<>();
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FabricanteDeProdutos that = (FabricanteDeProdutos) o;
        return Objects.equals(that.getUsername(), getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }

}
