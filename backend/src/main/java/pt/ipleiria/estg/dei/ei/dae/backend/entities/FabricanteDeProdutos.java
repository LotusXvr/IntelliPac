package pt.ipleiria.estg.dei.ei.dae.backend.entities;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllProducts",
                query = "SELECT p FROM Produto p ORDER BY p.nomeProduto"
        )
})
public class FabricanteDeProdutos extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nome;

    @OneToMany(mappedBy = "fabricante", fetch = FetchType.EAGER)
    private List<Produto> produtos;



    public FabricanteDeProdutos() {
        this.produtos = new ArrayList<>();
    }

    public FabricanteDeProdutos(String username, String password, String nome, String email) {
        super(username, password, nome, email);
        this.produtos = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nomeFabricante) {
        this.nome = nomeFabricante;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FabricanteDeProdutos that = (FabricanteDeProdutos) o;
        return Objects.equals(id, that.id) && Objects.equals(nome, that.nome) && Objects.equals(produtos, that.produtos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, produtos);
    }
}
