package pt.ipleiria.estg.dei.ei.dae.backend.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Produto extends ProdutoCatalogo implements Serializable {


    @Column(unique = true)
    private String serialNumber;
    @ManyToMany
    @JoinTable(
            name = "produto_embalagem",
            joinColumns = @JoinColumn(
                    // produto?
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
        generateSerialNumber();
        //sensoresDispositivos = new ArrayList<>();
    }

    public Produto(String nomeProduto, FabricanteDeProdutos fabricante) {
        super(nomeProduto, fabricante);
        this.embalagemDeProdutos = new ArrayList<>();
        generateSerialNumber();
    }

    @PrePersist
    private void generateSerialNumber() {
        // You can use a more sophisticated logic to generate serial numbers
        // For simplicity, let's assume a basic logic using the current timestamp and the product ID
        this.serialNumber = "SN" + System.currentTimeMillis() + "-" + this.getId();
    }
    public List<EmbalagemDeProduto> getEmbalagemDeProdutos() {
        return embalagemDeProdutos;
    }

    public void setEmbalagemDeProdutos(List<EmbalagemDeProduto> embalagemDeProdutos) {
        this.embalagemDeProdutos = embalagemDeProdutos;
    }
}
