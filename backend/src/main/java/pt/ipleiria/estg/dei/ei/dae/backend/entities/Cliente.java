package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllClientes",
                query = "SELECT c FROM Cliente c ORDER BY c.name" // JPQL
        )
})
public class Cliente extends User implements Serializable {
    @OneToMany(mappedBy = "consumidorFinal")
    private List<Encomenda> encomendas;

    public Cliente() {
    }

    public Cliente(String username, String password, String name, String email) {
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
