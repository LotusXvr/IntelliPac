package pt.ipleiria.estg.dei.ei.dae.backend.entities;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeProduto;

    @ManyToOne
    @JoinColumn(name = "fabricante_id")
    private FabricanteDeProdutos fabricante;

    @OneToMany(mappedBy = "produtoEmb")
    private List<Embalagem> embalagens;

    @OneToMany(mappedBy = "produto")
    private List<SensorDispositivo> sensoresDispositivos;

    public Produto() {
    }

    public Produto(String nomeProduto, FabricanteDeProdutos fabricante) {
        this.nomeProduto = nomeProduto;
        this.fabricante = fabricante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
