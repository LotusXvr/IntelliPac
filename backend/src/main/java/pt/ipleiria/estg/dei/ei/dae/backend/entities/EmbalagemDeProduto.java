package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class EmbalagemDeProduto extends Embalagem implements Serializable {

    private String tipoEmbalagem;

    @ManyToMany(mappedBy = "embalagemDeProdutos", fetch = FetchType.LAZY)
    private List<ProdutoFisico> produtos;

    public EmbalagemDeProduto() {
        this.produtos = new ArrayList<>();
    }

    public EmbalagemDeProduto(String material, String tipoEmbalagem) {
        super(material);
        this.tipoEmbalagem = tipoEmbalagem;
        this.produtos = new ArrayList<>();
    }

    public EmbalagemDeProduto(String material, String tipoEmbalagem, List<ProdutoFisico> produtos) {
        super(material);
        this.tipoEmbalagem = tipoEmbalagem;
        this.produtos = produtos;
    }

    public String getTipoEmbalagem() {
        return tipoEmbalagem;
    }

    public void setTipoEmbalagem(String tipoEmbalagem) {
        this.tipoEmbalagem = tipoEmbalagem;
    }

    public List<ProdutoFisico> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoFisico> produtos) {
        this.produtos = produtos;
    }

    public void addProduto(ProdutoFisico produto) {
        this.produtos.add(produto);
    }

    public void removeProduto(ProdutoFisico produto) {
        this.produtos.remove(produto);
    }
}
