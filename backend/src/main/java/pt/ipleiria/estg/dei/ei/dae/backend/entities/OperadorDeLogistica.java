package pt.ipleiria.estg.dei.ei.dae.backend.entities;

import jakarta.persistence.*;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.OperadorDeLogisticaBean;

import java.io.Serializable;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllOperadoresDeLogistica",
                query = "SELECT o FROM OperadorDeLogistica o ORDER BY o.name" // JPQL
        )
})
public class OperadorDeLogistica extends User implements Serializable {

    public OperadorDeLogistica() {
    }

    public OperadorDeLogistica(String username, String password, String name, String email) {
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
