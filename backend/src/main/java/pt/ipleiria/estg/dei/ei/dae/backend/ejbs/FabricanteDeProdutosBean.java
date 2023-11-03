package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.FabricanteDeProdutos;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;

@Stateless
public class FabricanteDeProdutosBean {
    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(String nomeFabricante) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(f.nomeFabricante) FROM FabricanteDeProdutos f WHERE f.nomeFabricante = :nomeFabricante",
                Long.class
        );
        query.setParameter("nomeFabricante", nomeFabricante);
        return (Long)query.getSingleResult() > 0L;
    }

    public void createFabricanteDeProdutos(String nomeFabricante) throws MyEntityExistsException {
        if (exists(nomeFabricante)) {
            throw new MyEntityExistsException("FabricanteDeProdutos with nomeFabricante '" + nomeFabricante + "' already exists");
        }

        FabricanteDeProdutos fabricanteDeProdutos = new FabricanteDeProdutos(nomeFabricante);
        entityManager.persist(fabricanteDeProdutos);
    }

    public FabricanteDeProdutos findFabricanteDeProdutosById(Long id) {
        return entityManager.find(FabricanteDeProdutos.class, id);
    }

    public void updateFabricanteDeProdutos(FabricanteDeProdutos fabricanteDeProdutos) {
        entityManager.merge(fabricanteDeProdutos);
    }

    public void removeFabricanteDeProdutos(FabricanteDeProdutos fabricanteDeProdutos) {
        entityManager.remove(entityManager.contains(fabricanteDeProdutos) ? fabricanteDeProdutos : entityManager.merge(fabricanteDeProdutos));
    }

    // Outros métodos conforme necessário para operações relacionadas a FabricanteDeProdutos
}
