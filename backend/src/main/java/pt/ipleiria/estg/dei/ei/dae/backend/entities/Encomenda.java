package pt.ipleiria.estg.dei.ei.dae.backend.entities;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllEncomendasCliente",
                query = "SELECT e FROM Encomenda e WHERE e.consumidorFinal = :cliente"
        ),
        @NamedQuery(
                name = "getAllEncomendasOperadoresLogistica",
                query = "SELECT e FROM Encomenda e WHERE e.operadorLogistica = :operador"
        )
})
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

    private String estado;

    // Outros atributos e getters/setters
    public Encomenda() {
    }

    public Encomenda(Cliente consumidorFinal, Date dataEncomenda, OperadorDeLogistica operadorLogistica, String estado) {
        this.consumidorFinal = consumidorFinal;
        this.dataEncomenda = dataEncomenda;
        this.operadorLogistica = operadorLogistica;
        this.estado = estado;
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

    public OperadorDeLogistica getOperadorLogistica() {
        return operadorLogistica;
    }

    public void setOperadorLogistica(OperadorDeLogistica operadorLogistica) {
        this.operadorLogistica = operadorLogistica;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
