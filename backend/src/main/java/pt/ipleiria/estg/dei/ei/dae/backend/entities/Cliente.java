package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

import java.io.Serializable;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllClientes",
                query = "SELECT c FROM Cliente c ORDER BY c.name" // JPQL
        )
})
public class Cliente extends User implements Serializable {

    public Cliente() {
    }

    public Cliente(String username, String password, String name, String email) {
        super(username, password, name, email);
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
}
