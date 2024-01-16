package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import pt.ipleiria.estg.dei.ei.dae.backend.entities.ProdutoFisico;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.TipoEmbalagemProduto;

import java.util.ArrayList;
import java.util.List;

public class ProdutoCatalogoDTO {

    private long id;
    private String nome;
    private String fabricanteUsername;
    private List<ProdutoFisicoDTO> produtos;

    private List<TipoEmbalagemDTO> embalagensACriar;

    private long peso;

    public ProdutoCatalogoDTO() {
        this.produtos = new ArrayList<>();
        this.embalagensACriar = new ArrayList<>();
    }

    public ProdutoCatalogoDTO(long id, String nome, String fabricanteUsername, long peso) {
        this.id = id;
        this.nome = nome;
        this.fabricanteUsername = fabricanteUsername;
        this.produtos = new ArrayList<>();
        this.embalagensACriar = new ArrayList<>();
        this.peso = peso;
    }

    public ProdutoCatalogoDTO(long id, String nome, String fabricanteUsername, long peso, List<TipoEmbalagemDTO> embalagensACriar) {
        this.id = id;
        this.nome = nome;
        this.fabricanteUsername = fabricanteUsername;
        this.produtos = new ArrayList<>();
        this.embalagensACriar = embalagensACriar;
        this.peso = peso;
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

    public long getPeso() {
        return peso;
    }

    public void setPeso(long peso) {
        this.peso = peso;
    }

    public List<TipoEmbalagemDTO> getEmbalagensACriar() {
        return embalagensACriar;
    }

    public void setEmbalagensACriar(List<TipoEmbalagemDTO> embalagensACriar) {
        this.embalagensACriar = embalagensACriar;
    }
}
