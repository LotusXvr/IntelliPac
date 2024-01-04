package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import pt.ipleiria.estg.dei.ei.dae.backend.entities.ProdutoCatalogo;

import java.util.ArrayList;
import java.util.List;

public class ProdutoFisicoDTO {
    private long id;
    private String nome;
    private String fabricanteUsername;
    private ProdutoCatalogo produtoCatalogo;

    public ProdutoFisicoDTO() {
    }

    public ProdutoFisicoDTO(long id, String nome, String fabricanteUsername, ProdutoCatalogo produtoCatalogo) {
        this.id = id;
        this.nome = nome;
        this.fabricanteUsername = fabricanteUsername;
        this.produtoCatalogo = produtoCatalogo;
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

    public ProdutoCatalogo getProdutoCatalogo() {
        return produtoCatalogo;
    }

    public void setProdutoCatalogo(ProdutoCatalogo produtoCatalogo) {
        this.produtoCatalogo = produtoCatalogo;
    }
}
