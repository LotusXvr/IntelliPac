package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.FabricanteDeProdutos;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.security.Hasher;

import java.util.List;

@Stateless
public class FabricanteDeProdutosBean {
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Hasher hasher;

    public boolean exists(String nome) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(f.name) FROM FabricanteDeProdutos f WHERE f.name = :nomeFabricante",
                Long.class
        );
        query.setParameter("nomeFabricante", nome);
        return (Long)query.getSingleResult() > 0L;
    }

    public void create(String username, String password, String nome, String email) throws MyEntityExistsException {
        if (exists(nome)) {
            throw new MyEntityExistsException("FabricanteDeProdutos with name '" + nome + "' already exists");
        }

        FabricanteDeProdutos fabricanteDeProdutos = null;

        try {
            fabricanteDeProdutos = new FabricanteDeProdutos(username, hasher.hash(password), nome, email);
            entityManager.persist(fabricanteDeProdutos);
        } catch (ConstraintViolationException e) {
            throw new MyEntityExistsException(e.getMessage());
        }
    }

    public FabricanteDeProdutos find(String username) {
        var fabricanteDeProdutos = entityManager.find(FabricanteDeProdutos.class, username);
        if (fabricanteDeProdutos == null) {
            throw new IllegalArgumentException("FabricanteDeProdutos with id '" + username + "' not found");
        }
        Hibernate.initialize(fabricanteDeProdutos.getProdutos());

        return fabricanteDeProdutos;
    }

    public void update(FabricanteDeProdutos fabricanteDeProdutos) {
        entityManager.merge(fabricanteDeProdutos);
    }

    public void remove(FabricanteDeProdutos fabricanteDeProdutos) {
        entityManager.remove(entityManager.contains(fabricanteDeProdutos) ? fabricanteDeProdutos : entityManager.merge(fabricanteDeProdutos));
    }

    public List<FabricanteDeProdutos> getAllFabricantes() {
        Query query = entityManager.createQuery("SELECT f FROM FabricanteDeProdutos f ORDER BY f.name");
        return query.getResultList();
    }


}
