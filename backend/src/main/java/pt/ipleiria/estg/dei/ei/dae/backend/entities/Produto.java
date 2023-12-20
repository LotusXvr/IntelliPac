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

    @OneToMany(mappedBy = "produtoEmb")
    private List<Embalagem> embalagens;

    @OneToMany(mappedBy = "produto")
    private List<SensorDispositivo> sensoresDispositivos;

    public Produto() {
        embalagens = new ArrayList<>();
        sensoresDispositivos = new ArrayList<>();
    }

    public Produto(String nomeProduto, FabricanteDeProdutos fabricante) {
        this.nomeProduto = nomeProduto;
        this.fabricante = fabricante;
        embalagens = new ArrayList<>();
        sensoresDispositivos = new ArrayList<>();
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

    public long getFabricanteId() {
        return fabricante.getId();
    }

    public FabricanteDeProdutos getFabricante() {
        return fabricante;
    }

    public void setFabricante(FabricanteDeProdutos fabricante) {
        this.fabricante = fabricante;
    }

    public List<Embalagem> getEmbalagens() {
        return embalagens;
    }

    public void setEmbalagens(List<Embalagem> embalagens) {
        this.embalagens = embalagens;
    }

    public List<SensorDispositivo> getSensoresDispositivos() {
        return sensoresDispositivos;
    }

    public void setSensoresDispositivos(List<SensorDispositivo> sensoresDispositivos) {
        this.sensoresDispositivos = sensoresDispositivos;
    }
}
