package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.*;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class EmbalagemDeProdutoBean {
    @PersistenceContext
    private EntityManager entityManager;

    public EmbalagemDeProduto create(String material, String tipoEmbalagem) {
        EmbalagemDeProduto embalagemDeProduto = new EmbalagemDeProduto(material,tipoEmbalagem);
        entityManager.persist(embalagemDeProduto);
        return embalagemDeProduto;
    }

    public EmbalagemDeProduto find(Long id) {
        return entityManager.find(EmbalagemDeProduto.class, id);
    }

    public void update(long id, String material, String tipoEmbalagem) throws MyEntityNotFoundException {
        EmbalagemDeProduto embalagemDeProduto = find(id);
        if (embalagemDeProduto == null) {
            throw new MyEntityNotFoundException("Produto catálogo com id " + id + " não existe");
        }

        embalagemDeProduto.setMaterial(material);
        embalagemDeProduto.setTipoEmbalagem(tipoEmbalagem);
        entityManager.merge(embalagemDeProduto);
    }

    public void remove(long id) throws MyEntityNotFoundException {
        EmbalagemDeProduto embalagemDeProduto = find(id);
        if (embalagemDeProduto == null) {
            throw new MyEntityNotFoundException("Produto catálogo com id " + id + " não existe");
        }
        entityManager.remove(embalagemDeProduto);
    }

    public List<EmbalagemDeProduto> getAllEmbalagensDeProduto() {
        return entityManager.createNamedQuery("getAllEmbalagensDeProduto", EmbalagemDeProduto.class).getResultList();
    }


    public void associateSensorToEmbalagem(long idEmbalagem, long idSensor) throws MyEntityNotFoundException {
        EmbalagemDeProduto embalagemDeProduto = find(idEmbalagem);
        if (embalagemDeProduto == null) {
            throw new MyEntityNotFoundException("Embalagem with id " + idEmbalagem + " not found");
        }
        Sensor sensor = entityManager.find(Sensor.class, idSensor);
        if (sensor == null) {
            throw new MyEntityNotFoundException("Sensor with id " + idSensor + " not found");
        }
        embalagemDeProduto.addSensor(sensor);
        entityManager.merge(embalagemDeProduto);
    }

    public void removeSensorFromEmbalagem(long idEmbalagem, long idSensor) throws MyEntityNotFoundException {
        EmbalagemDeProduto embalagemDeProduto = find(idEmbalagem);
        if (embalagemDeProduto == null) {
            throw new MyEntityNotFoundException("Embalagem with id " + idEmbalagem + " not found");
        }
        Sensor sensor = entityManager.find(Sensor.class, idSensor);
        if (sensor == null) {
            throw new MyEntityNotFoundException("Sensor with id " + idSensor + " not found");
        }
        embalagemDeProduto.removeSensor(sensor);
        entityManager.merge(embalagemDeProduto);
    }

    public EmbalagemDeProduto getEmbalagemDeProdutoWithSensor(long id) {
        EmbalagemDeProduto embalagemDeProduto = find(id);
        if (embalagemDeProduto != null) {
            Hibernate.initialize(embalagemDeProduto.getSensores());
        }
        return embalagemDeProduto;
    }
}
