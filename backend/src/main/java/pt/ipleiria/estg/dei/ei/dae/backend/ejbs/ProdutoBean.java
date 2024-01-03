package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Produto;

@Stateless
public class ProdutoBean {
    @PersistenceContext
    private EntityManager entityManager;

    public Produto find(String username) {
        return entityManager.find(Produto.class, username);
    }
    public Produto findOrFail(String nomeProduto) {
        var produto = entityManager.getReference(Produto.class, nomeProduto);
        Hibernate.initialize(produto);
        return produto;
    }
}
