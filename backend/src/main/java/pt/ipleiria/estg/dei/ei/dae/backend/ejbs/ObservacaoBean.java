package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Observacao;

@Stateless
public class ObservacaoBean {

    @PersistenceContext
    private EntityManager entityManager;

    public void create(Observacao observacao) {
        entityManager.persist(observacao);
    }

    public Observacao find(Long id) {
        return entityManager.find(Observacao.class, id);
    }

    public void update(Observacao observacao) {
        entityManager.merge(observacao);
    }

    public void remove(Observacao observacao) {
        entityManager.remove(entityManager.contains(observacao) ? observacao : entityManager.merge(observacao));
    }


}
