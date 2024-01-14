package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Produto;

import java.util.List;

@Stateless
public class ProdutoBean {
    @PersistenceContext
    private EntityManager entityManager;

    public Produto find(long id) {
        return entityManager.find(Produto.class, id);
    }
    public Produto findOrFail(long id) {
        var produto = entityManager.getReference(Produto.class, id);
        Hibernate.initialize(produto);
        return produto;
    }
}
