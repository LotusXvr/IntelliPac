package pt.ipleiria.estg.dei.ei.dae.backend.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Entity
public class Produto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    protected String nomeProduto;

    @ManyToOne
    @JoinColumn(name = "fabricante_id")
    private FabricanteDeProdutos fabricante;

    private long peso;

    public Produto() {
    }

    public Produto(String nomeProduto, FabricanteDeProdutos fabricante, long peso) {
        this.nomeProduto = nomeProduto;
        this.fabricante = fabricante;
        this.peso = peso;
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

    public long getPeso() {
        return peso;
    }

    public void setPeso(long peso) {
        this.peso = peso;
    }
}
