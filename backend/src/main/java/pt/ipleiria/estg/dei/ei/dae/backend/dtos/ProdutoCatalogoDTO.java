package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import pt.ipleiria.estg.dei.ei.dae.backend.entities.ProdutoFisico;

import java.util.ArrayList;
import java.util.List;

public class ProdutoCatalogoDTO {

    private long id;
    private String nome;
    private String fabricanteUsername;
    private List<ProdutoFisicoDTO> produtos;

    public ProdutoCatalogoDTO() {
        this.produtos = new ArrayList<>();
    }

    public ProdutoCatalogoDTO(long id, String nome, String fabricanteUsername) {
        this.id = id;
        this.nome = nome;
        this.fabricanteUsername = fabricanteUsername;
        this.produtos = new ArrayList<>();
    }

    public ProdutoCatalogoDTO(long id, String nome, String fabricanteUsername, List<ProdutoFisicoDTO> produtos) {
        this.id = id;
        this.nome = nome;
        this.fabricanteUsername = fabricanteUsername;
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

    public String getFabricanteUsername() {
        return fabricanteUsername;
    }

    public void setFabricanteUsername(String fabricanteUsername) {
        this.fabricanteUsername = fabricanteUsername;
    }

    public List<ProdutoFisicoDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoFisicoDTO> produtos) {
        this.produtos = produtos;
    }
}
