package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import pt.ipleiria.estg.dei.ei.dae.backend.entities.Produto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FabricanteProdutoDTO implements Serializable {
    private long id;
    private String nome;
    private List<ProdutoDTO> produtos;
    private String username;
    private String password;
    private String email;

    public FabricanteProdutoDTO() {
        this.produtos = new ArrayList<>();
    }

    public FabricanteProdutoDTO(String username, String password, String nome, String email, long id, List<ProdutoDTO> produtos) {
        this.username = username;
        this.password = password;
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.produtos = produtos;
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

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<ProdutoDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoDTO> produtos) {
        this.produtos = produtos;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
