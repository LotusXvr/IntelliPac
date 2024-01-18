package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllEmbalagensDeTransporte",
                query = "SELECT p FROM EmbalagemDeTransporte p ORDER BY p.id"
        )
})
public class EmbalagemDeTransporte extends Embalagem implements Serializable {
    @ManyToMany(mappedBy = "embalagensTransporte")
    private List<Encomenda> encomendas;

    @Min(value = 0, message = "A coluna deve ter apenas os valores 0 ou 1")
    @Max(value = 1, message = "A coluna deve ter apenas os valores 0 ou 1")
    // 0 - não está em uso, 1 - está em uso
    private int estado;

    public EmbalagemDeTransporte() {
        this.encomendas = new ArrayList<>();
    }

    public EmbalagemDeTransporte(String material, long altura, long largura, long comprimento, int estado) {
        super(material, altura, largura, comprimento);
        this.estado = estado;
        this.encomendas = new ArrayList<>();
    }

    public List<Encomenda> getEncomendas() {
        return encomendas;
    }

    public void setEncomendas(List<Encomenda> encomendas) {
        this.encomendas = encomendas;
    }

    public void addEncomenda(Encomenda encomenda){
        this.encomendas.add(encomenda);
    }
    public void removeEncomenda(Encomenda encomenda){
        this.encomendas.remove(encomenda);
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}

