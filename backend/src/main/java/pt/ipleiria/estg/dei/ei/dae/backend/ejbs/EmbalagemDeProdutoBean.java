package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.EmbalagemDeProduto;

@Stateless
public class EmbalagemDeProdutoBean {
    @PersistenceContext
    private EntityManager entityManager;

    public void create(EmbalagemDeProduto embalagemDeProduto) {
        entityManager.persist(embalagemDeProduto);
    }

    public EmbalagemDeProduto find(Long id) {
        return entityManager.find(EmbalagemDeProduto.class, id);
    }

    public void update(EmbalagemDeProduto embalagemDeProduto) {
        entityManager.merge(embalagemDeProduto);
    }

    public void remove(EmbalagemDeProduto embalagemDeProduto) {
        entityManager.remove(entityManager.contains(embalagemDeProduto) ? embalagemDeProduto : entityManager.merge(embalagemDeProduto));
    }
}
