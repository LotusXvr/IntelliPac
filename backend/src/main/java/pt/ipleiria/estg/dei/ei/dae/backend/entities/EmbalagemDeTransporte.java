package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class EmbalagemDeTransporte extends Embalagem implements Serializable {
    @ManyToOne
    @JoinColumn(name = "encomenda_id")
    private Encomenda encomenda;

    public EmbalagemDeTransporte() {
    }

    public EmbalagemDeTransporte(String material, Encomenda encomenda) {
        super(material);
        this.encomenda = encomenda;
    }

    public Encomenda getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(Encomenda encomenda) {
        this.encomenda = encomenda;
    }
}

