package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;

@Entity
public class EmbalagemDeTransporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "encomenda_id")
    private Encomenda encomenda;

    // Outros atributos e getters/setters
    public EmbalagemDeTransporte() {
    }

    public EmbalagemDeTransporte(Encomenda encomenda) {
        this.encomenda = encomenda;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Encomenda getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(Encomenda encomenda) {
        this.encomenda = encomenda;
    }
}

