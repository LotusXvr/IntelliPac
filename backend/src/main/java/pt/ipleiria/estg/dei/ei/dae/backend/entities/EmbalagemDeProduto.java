package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllEmbalagensDeProduto",
                query = "SELECT p FROM EmbalagemDeProduto p ORDER BY p.id"
        )
})
public class EmbalagemDeProduto extends Embalagem implements Serializable {

    @Pattern(regexp = "[1-3]", message = "A coluna deve ter apenas os valores 1, 2 ou 3") //1-Primaria, 2-Secundaria, 3-Terciaria
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
