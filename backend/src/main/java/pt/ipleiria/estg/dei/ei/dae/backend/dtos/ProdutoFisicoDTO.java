package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import java.util.ArrayList;
import java.util.List;

public class ProdutoFisicoDTO {
    private long id;
    private String nome;
    private String fabricanteUsername;
    private long produtoCatalogoId;

    private long encomendaId;

    private List<EmbalagemDeProdutoDTO> embalagensDeProduto;

    public ProdutoFisicoDTO() {
        this.embalagensDeProduto = new ArrayList<>();
    }

    public ProdutoFisicoDTO(long id, String nome, String fabricanteUsername, long produtoCatalogoId, long encomendaId) {
        this.id = id;
        this.nome = nome;
        this.fabricanteUsername = fabricanteUsername;
        this.produtoCatalogoId = produtoCatalogoId;
        this.encomendaId = encomendaId;
        this.embalagensDeProduto = new ArrayList<>();
    }

    public List<EmbalagemDeProdutoDTO> getEmbalagensDeProduto() {
        return embalagensDeProduto;
    }

    public void setEmbalagensDeProduto(List<EmbalagemDeProdutoDTO> embalagensDeProduto) {
        this.embalagensDeProduto = embalagensDeProduto;
    }

    public void addEmbalagemDeProduto(EmbalagemDeProdutoDTO embalagemDeProduto) {
        this.embalagensDeProduto.add(embalagemDeProduto);
    }

    public void removeEmbalagemDeProduto(EmbalagemDeProdutoDTO embalagemDeProduto) {
        this.embalagensDeProduto.remove(embalagemDeProduto);
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

    public long getProdutoCatalogoId() {
        return produtoCatalogoId;
    }

    public void setProdutoCatalogoId(long produtoCatalogoId) {
        this.produtoCatalogoId = produtoCatalogoId;
    }

    public long getEncomendaId() {
        return encomendaId;
    }

    public void setEncomendaId(long encomendaId) {
        this.encomendaId = encomendaId;
    }
}
