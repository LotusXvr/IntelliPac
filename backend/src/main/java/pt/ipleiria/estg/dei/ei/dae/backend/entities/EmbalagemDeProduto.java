package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @Min(value = 1, message = "A coluna deve ter apenas os valores 1, 2 ou 3")
    @Max(value = 3, message = "A coluna deve ter apenas os valores 1, 2 ou 3")
    private long tipoEmbalagem;

    @ManyToMany(mappedBy = "embalagemDeProdutos", fetch = FetchType.LAZY)
    private List<ProdutoFisico> produtos;

    public EmbalagemDeProduto() {
        this.produtos = new ArrayList<>();
    }

    public EmbalagemDeProduto(String material, long tipoEmbalagem) {
        super(material);
        this.tipoEmbalagem = tipoEmbalagem;
        this.produtos = new ArrayList<>();
    }

    public EmbalagemDeProduto(String material, long tipoEmbalagem, List<ProdutoFisico> produtos) {
        super(material);
        this.tipoEmbalagem = tipoEmbalagem;
        this.produtos = produtos;
    }

    public long getTipoEmbalagem() {
        return tipoEmbalagem;
    }

    public void setTipoEmbalagem(long tipoEmbalagem) {
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
