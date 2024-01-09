package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.OperadorDeLogisticaBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllOperadoresDeLogistica",
                query = "SELECT o FROM OperadorDeLogistica o ORDER BY o.name" // JPQL
        )
})
public class OperadorDeLogistica extends User implements Serializable {
    @OneToMany(mappedBy = "operadorLogistica")
    private List<Encomenda> encomendas;

    public OperadorDeLogistica() {
    }

    public OperadorDeLogistica(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.encomendas = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OperadorDeLogistica that = (OperadorDeLogistica) o;
        return Objects.equals(that.getUsername(), getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }

    public List<Encomenda> getEncomendas() {
        return encomendas;
    }

    public void setEncomendas(List<Encomenda> encomendas) {
        this.encomendas = encomendas;
    }
    public void addEncomenda(Encomenda encomenda) {
        this.encomendas.add(encomenda);
    }

    public void removeEncomenda(Encomenda encomenda) {
        this.encomendas.remove(encomenda);
    }
}
