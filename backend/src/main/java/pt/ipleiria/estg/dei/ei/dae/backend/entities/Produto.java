package pt.ipleiria.estg.dei.ei.dae.backend.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Produto extends ProdutoCatalogo implements Serializable {

    @Transient
    private Long serialNumber;
    @ManyToMany
    @JoinTable(
            name = "produto_embalagem",
            joinColumns = @JoinColumn(
                    name = "product_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "embalagem_id",
                    referencedColumnName = "id"
            )
    )
    private List<EmbalagemDeProduto> embalagemDeProdutos;

    //  @OneToMany(mappedBy = "produto")
   // private List<SensorDispositivo> sensoresDispositivos;

    public Produto() {
        embalagemDeProdutos = new ArrayList<>();
        //sensoresDispositivos = new ArrayList<>();
    }

    public Produto(String nomeProduto, FabricanteDeProdutos fabricante) {
        super(nomeProduto, fabricante);
        this.nomeProduto = nomeProduto;
        this.embalagemDeProdutos = new ArrayList<>();
    }

    @Override
    public String getNomeProduto() {
        return nomeProduto + " - " +serialNumber;
    }
    public List<EmbalagemDeProduto> getEmbalagemDeProdutos() {
        return embalagemDeProdutos;
    }

    public void setEmbalagemDeProdutos(List<EmbalagemDeProduto> embalagemDeProdutos) {
        this.embalagemDeProdutos = embalagemDeProdutos;
    }

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produto_sequence")
    @SequenceGenerator(name = "produto_sequence", sequenceName = "produto_sequence", allocationSize = 1)
    @Column(unique = true, nullable = false)
    public Long getSerialNumber() {
        return serialNumber;
    }

}
