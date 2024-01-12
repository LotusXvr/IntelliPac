package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
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

    public Embalagem findEmbalagemDetails(long id) throws MyEntityNotFoundException {
        Embalagem embalagem = entityManager.find(Embalagem.class, id);
        if (embalagem != null) {
            Hibernate.initialize(embalagem.getSensores());
        }

        return embalagem;
    }

    public Embalagem create(String material) {
        Embalagem embalagem = new Embalagem(material);
        entityManager.persist(embalagem);
        return embalagem;
    }

    public Embalagem update(long id, String material) throws MyEntityNotFoundException {
        Embalagem embalagem = find(id);
        if (embalagem == null) {
            throw new MyEntityNotFoundException("Embalagem with id " + id + " not found");
        }
        embalagem.setMaterial(material);
        entityManager.merge(embalagem);
        return embalagem;
    }

    public void delete(long id) throws MyEntityNotFoundException {
        Embalagem embalagem = find(id);
        if (embalagem == null) {
            throw new MyEntityNotFoundException("Embalagem with id " + id + " not found");
        }
        entityManager.remove(embalagem);
    }

    public void associateSensorToEmbalagem(long idEmbalagem, long idSensor) throws MyEntityNotFoundException {
        Embalagem embalagem = find(idEmbalagem);
        if (embalagem == null) {
            throw new MyEntityNotFoundException("Embalagem with id " + idEmbalagem + " not found");
        }
        Sensor sensor = entityManager.find(Sensor.class, idSensor);
        if (sensor == null) {
            throw new MyEntityNotFoundException("Sensor with id " + idSensor + " not found");
        }
        embalagem.addSensor(sensor);
        entityManager.merge(embalagem);
    }

    public void removeSensorFromEmbalagem(long idEmbalagem, long idSensor) throws MyEntityNotFoundException {
        Embalagem embalagem = find(idEmbalagem);
        if (embalagem == null) {
            throw new MyEntityNotFoundException("Embalagem with id " + idEmbalagem + " not found");
        }
        Sensor sensor = entityManager.find(Sensor.class, idSensor);
        if (sensor == null) {
            throw new MyEntityNotFoundException("Sensor with id " + idSensor + " not found");
        }
        embalagem.removeSensor(sensor);
        entityManager.merge(embalagem);
    }

}
