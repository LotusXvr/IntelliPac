package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import java.io.Serializable;

public class ProdutoDTO implements Serializable {
    private long id;
    private String nome;
    private long idFabricante; // Referência ao Fabricante de Produtos
    private String nomeFabricante;

    public ProdutoDTO() {
    }

    public ProdutoDTO(long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public ProdutoDTO(long id, String nome, long idFabricante) {
        this.id = id;
        this.nome = nome;
        this.idFabricante = idFabricante;
    }

    public ProdutoDTO(long id, String nome, long idFabricante, String nomeFabricante) {
        this.id = id;
        this.nome = nome;
        this.idFabricante = idFabricante;
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

    public long getIdFabricante() {
        return idFabricante;
    }

    public void setIdFabricante(long idFabricante) {
        this.idFabricante = idFabricante;
    }

    public String getNomeFabricante() {
        return nomeFabricante;
    }

    public void setNomeFabricante(String nomeFabricante) {
        this.nomeFabricante = nomeFabricante;
    }
}

