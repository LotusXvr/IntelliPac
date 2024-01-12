package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EmbalagemDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Embalagem;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class EmbalagemBean {
    @PersistenceContext
    private EntityManager entityManager;

    public Embalagem find(long id) throws MyEntityNotFoundException {
        Embalagem embalagem = entityManager.find(Embalagem.class, id);
        if (embalagem == null) {
            throw new MyEntityNotFoundException("Embalagem with id " + id + " not found");
        }
        return embalagem;
    }

    public List<Embalagem> getAll() {
        return entityManager.createNamedQuery("getAllEmbalagem", Embalagem.class).getResultList();
    }

    public Embalagem create(String material) {
        Embalagem embalagem = new Embalagem(material);
        entityManager.persist(embalagem);
        return embalagem;
    }

}
