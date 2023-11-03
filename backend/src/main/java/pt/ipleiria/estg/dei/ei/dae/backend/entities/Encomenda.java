package pt.ipleiria.estg.dei.ei.dae.backend.entities;


import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class Encomenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String consumidorFinal;
    private Date dataEncomenda;

    @OneToMany(mappedBy = "encomenda")
    private List<EmbalagemDeTransporte> embalagensTransporte;

    @ManyToMany
    @JoinTable(name = "encomenda_produtos",
            joinColumns = @JoinColumn(name = "encomenda_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id"))
    private List<Produto> produtos;

    // Outros atributos e getters/setters
    public Encomenda() {
    }

    public Encomenda(String consumidorFinal, Date dataEncomenda) {
        this.consumidorFinal = consumidorFinal;
        this.dataEncomenda = dataEncomenda;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConsumidorFinal() {
        return consumidorFinal;
    }

    public void setConsumidorFinal(String consumidorFinal) {
        this.consumidorFinal = consumidorFinal;
    }

    public Date getDataEncomenda() {
        return dataEncomenda;
    }

    public void setDataEncomenda(Date dataEncomenda) {
        this.dataEncomenda = dataEncomenda;
    }

    public List<EmbalagemDeTransporte> getEmbalagensTransporte() {
        return embalagensTransporte;
    }

    public void setEmbalagensTransporte(List<EmbalagemDeTransporte> embalagensTransporte) {
        this.embalagensTransporte = embalagensTransporte;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }
}
