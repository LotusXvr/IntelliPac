package pt.ipleiria.estg.dei.ei.dae.backend.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Produto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nomeProduto;

    @ManyToOne
    @JoinColumn(name = "fabricante_id")
    @NotNull
    private FabricanteDeProdutos fabricante;

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
        this.nomeProduto = nomeProduto;
        this.fabricante = fabricante;
        this.embalagemDeProdutos = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public FabricanteDeProdutos getFabricante() {
        return fabricante;
    }

    public void setFabricante(FabricanteDeProdutos fabricante) {
        this.fabricante = fabricante;
    }

    public List<EmbalagemDeProduto> getEmbalagemDeProdutos() {
        return embalagemDeProdutos;
    }

    public void setEmbalagemDeProdutos(List<EmbalagemDeProduto> embalagemDeProdutos) {
        this.embalagemDeProdutos = embalagemDeProdutos;
    }
}
