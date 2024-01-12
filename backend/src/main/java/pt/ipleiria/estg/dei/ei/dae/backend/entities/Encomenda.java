package pt.ipleiria.estg.dei.ei.dae.backend.entities;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    @Column(nullable = false)
    private String dataEncomenda;

    @ManyToMany
    @JoinTable(
            name = "Encomenda_Embalagem",
            joinColumns = @JoinColumn(name = "encomenda_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "embalagem_id", referencedColumnName = "id")
    )
    private List<EmbalagemDeTransporte> embalagensTransporte;

    @OneToMany(mappedBy = "encomenda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoFisico> produtos;

    private String estado;

    // Outros atributos e getters/setters
    public Encomenda() {
        this.produtos = new ArrayList<>();
        this.embalagensTransporte = new ArrayList<>();
    }

    public Encomenda(Cliente consumidorFinal, String dataEncomenda, OperadorDeLogistica operadorLogistica, String estado) {
        this.consumidorFinal = consumidorFinal;
        this.dataEncomenda = dataEncomenda;
        this.operadorLogistica = operadorLogistica;
        this.estado = estado;
        this.produtos = new ArrayList<>();
        this.embalagensTransporte = new ArrayList<>();
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

    public String getDataEncomenda() {
        return dataEncomenda;
    }

    public void setDataEncomenda(String dataEncomenda) {
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

    public void addEmbalagemTransporte(EmbalagemDeTransporte embalagemDeTransporte) {
        this.embalagensTransporte.add(embalagemDeTransporte);
    }

    public void removeEmbalagemTransporte(EmbalagemDeTransporte embalagemDeTransporte) {
        this.embalagensTransporte.remove(embalagemDeTransporte);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Encomenda encomenda = (Encomenda) o;
        return Objects.equals(id, encomenda.id) && Objects.equals(consumidorFinal, encomenda.consumidorFinal) && Objects.equals(operadorLogistica, encomenda.operadorLogistica) && Objects.equals(dataEncomenda, encomenda.dataEncomenda) && Objects.equals(embalagensTransporte, encomenda.embalagensTransporte) && Objects.equals(produtos, encomenda.produtos) && Objects.equals(estado, encomenda.estado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, consumidorFinal, operadorLogistica, dataEncomenda, embalagensTransporte, produtos, estado);
    }
}
