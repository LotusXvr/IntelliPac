package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.EmbalagemDeTransporte;

@Stateless
public class EmbalagemDeTransporteBean {
    @PersistenceContext
    private EntityManager entityManager;

    public void createEmbalagemDeTransporte(EmbalagemDeTransporte embalagemDeTransporte) {
        entityManager.persist(embalagemDeTransporte);
    }

    public EmbalagemDeTransporte findEmbalagemDeTransporteById(Long id) {
        return entityManager.find(EmbalagemDeTransporte.class, id);
    }

    public void updateEmbalagemDeTransporte(EmbalagemDeTransporte embalagemDeTransporte) {
        entityManager.merge(embalagemDeTransporte);
    }

    public void removeEmbalagemDeTransporte(EmbalagemDeTransporte embalagemDeTransporte) {
        entityManager.remove(entityManager.contains(embalagemDeTransporte) ? embalagemDeTransporte : entityManager.merge(embalagemDeTransporte));
    }

}
