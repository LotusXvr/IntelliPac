package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.FabricanteDeProdutos;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;

import java.util.List;

@Stateless
public class FabricanteDeProdutosBean {
    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(String nome) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(f.nome) FROM FabricanteDeProdutos f WHERE f.nome = :nomeFabricante",
                Long.class
        );
        query.setParameter("nomeFabricante", nome);
        return (Long)query.getSingleResult() > 0L;
    }

    public void create(String nomeFabricante) throws MyEntityExistsException {
        if (exists(nomeFabricante)) {
            throw new MyEntityExistsException("FabricanteDeProdutos with nomeFabricante '" + nomeFabricante + "' already exists");
        }

        FabricanteDeProdutos fabricanteDeProdutos = new FabricanteDeProdutos(nomeFabricante);
        entityManager.persist(fabricanteDeProdutos);
    }

    public FabricanteDeProdutos find(long id) {
        var fabricanteDeProdutos = entityManager.find(FabricanteDeProdutos.class, id);
        if (fabricanteDeProdutos == null) {
            throw new IllegalArgumentException("FabricanteDeProdutos with id '" + id + "' not found");
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
        Query query = entityManager.createQuery("SELECT f FROM FabricanteDeProdutos f ORDER BY f.nome");
        return query.getResultList();
    }

}
