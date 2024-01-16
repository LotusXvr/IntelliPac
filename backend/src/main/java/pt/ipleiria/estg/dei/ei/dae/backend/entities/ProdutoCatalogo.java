package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllProductsCatalogo",
                query = "SELECT p FROM ProdutoCatalogo p ORDER BY p.nomeProduto"
        )
})
public class ProdutoCatalogo extends Produto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "produtoCatalogo")
    private List<ProdutoFisico> produtos;
    private long peso;
    @ManyToMany
    @JoinTable(
            name = "produtoCatalogo_embalagem",
            joinColumns = @JoinColumn(
                    name = "produto_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "embalagem_id",
                    referencedColumnName = "id"
            )
    )
    private List<TipoEmbalagemProduto> embalagensACriar;

    public ProdutoCatalogo() {
        this.produtos = new ArrayList<>();
        this.embalagensACriar = new ArrayList<>();
    }

    public ProdutoCatalogo(String nomeProduto, FabricanteDeProdutos fabricante, long peso) {
        super(nomeProduto, fabricante);
        this.peso = peso;
        this.produtos = new ArrayList<>();
        this.embalagensACriar = new ArrayList<>();
    }

    public ProdutoCatalogo(String nomeProduto, FabricanteDeProdutos fabricante, long peso, List<TipoEmbalagemProduto> embalagensACriar) {
        super(nomeProduto, fabricante);
        this.peso = peso;
        this.produtos = new ArrayList<>();
        this.embalagensACriar = embalagensACriar;
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

    public long getPeso() {
        return peso;
    }

    public void setPeso(long peso) {
        this.peso = peso;
    }

    public List<TipoEmbalagemProduto> getEmbalagensACriar() {
        return embalagensACriar;
    }

    public void setEmbalagensACriar(List<TipoEmbalagemProduto> embalagensACriar) {
        this.embalagensACriar = embalagensACriar;
    }

    public void addEmbalagemACriar(TipoEmbalagemProduto embalagem) {
        this.embalagensACriar.add(embalagem);
    }

    public void removeEmbalagemACriar(TipoEmbalagemProduto embalagem) {
        this.embalagensACriar.remove(embalagem);
    }

}
