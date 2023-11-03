package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Encomenda;

@Stateless
public class EncomendaBean {
    @PersistenceContext
    private EntityManager entityManager;

    public void createEncomenda(Encomenda encomenda) {
        entityManager.persist(encomenda);
    }

    public Encomenda findEncomendaById(Long id) {
        return entityManager.find(Encomenda.class, id);
    }

    public void updateEncomenda(Encomenda encomenda) {
        entityManager.merge(encomenda);
    }

    public void removeEncomenda(Encomenda encomenda) {
        entityManager.remove(entityManager.contains(encomenda) ? encomenda : entityManager.merge(encomenda));
    }
}
