package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Embalagem;

@Stateless
public class EmbalagemBean {
    @PersistenceContext
    private EntityManager entityManager;

    public void createEmbalagem(Embalagem embalagem) {
        entityManager.persist(embalagem);
    }

    public Embalagem findEmbalagemById(Long id) {
        return entityManager.find(Embalagem.class, id);
    }

    public void updateEmbalagem(Embalagem embalagem) {
        entityManager.merge(embalagem);
    }

    public void removeEmbalagem(Embalagem embalagem) {
        entityManager.remove(entityManager.contains(embalagem) ? embalagem : entityManager.merge(embalagem));
    }
}
