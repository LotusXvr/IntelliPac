package pt.ipleiria.estg.dei.ei.dae.backend.entities;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Encomenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente consumidorFinal;
    @ManyToOne
    @JoinColumn(name = "operador_logistica_id")
    private OperadorDeLogistica operadorLogistica;
    private Date dataEncomenda;

    @OneToMany(mappedBy = "encomenda")
    private List<EmbalagemDeTransporte> embalagensTransporte;

    @OneToMany(mappedBy = "encomenda")
    private List<ProdutoFisico> produtos;

    // Outros atributos e getters/setters
    public Encomenda() {
    }

    public Encomenda(Cliente consumidorFinal, Date dataEncomenda, OperadorDeLogistica operadorLogistica) {
        this.consumidorFinal = consumidorFinal;
        this.dataEncomenda = dataEncomenda;
        this.operadorLogistica = operadorLogistica;
        this.produtos = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getConsumidorFinal() {
        return consumidorFinal;
    }

    public void setConsumidorFinal(Cliente consumidorFinal) {
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
