package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import java.io.Serializable;

public class ProdutoDTO implements Serializable {
    private long id;
    private String nome;
    private String fabricanteUsername; // Referência ao Fabricante de Produtos
    private String nomeFabricante;

    public ProdutoDTO() {
    }

    public ProdutoDTO(long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public ProdutoDTO(long id, String nome, String fabricanteUsername) {
        this.id = id;
        this.nome = nome;
        this.fabricanteUsername = fabricanteUsername;
    }

    public ProdutoDTO(long id, String nome, String fabricanteUsername, String nomeFabricante) {
        this.id = id;
        this.nome = nome;
        this.fabricanteUsername = fabricanteUsername;
        this.nomeFabricante = nomeFabricante;
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

    public String getNomeFabricante() {
        return nomeFabricante;
    }

    public void setNomeFabricante(String nomeFabricante) {
        this.nomeFabricante = nomeFabricante;
    }
}

