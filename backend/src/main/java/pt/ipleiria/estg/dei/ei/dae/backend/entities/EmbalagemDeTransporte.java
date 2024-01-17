package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;

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

    public EmbalagemDeTransporte() {
        this.encomendas = new ArrayList<>();
    }

    public EmbalagemDeTransporte(String material, long altura, long largura, long comprimento) {
        super(material, altura, largura, comprimento);
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
}

