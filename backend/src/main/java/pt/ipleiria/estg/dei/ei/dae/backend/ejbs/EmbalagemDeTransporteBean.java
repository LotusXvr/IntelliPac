package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.*;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class EmbalagemDeTransporteBean {
    @PersistenceContext
    private EntityManager entityManager;

    public EmbalagemDeTransporte create(String material) {
        EmbalagemDeTransporte embalagemDeTransporte = new EmbalagemDeTransporte(material);
        entityManager.persist(embalagemDeTransporte);
        return embalagemDeTransporte;
    }

    public EmbalagemDeTransporte find(long id) {
        return entityManager.find(EmbalagemDeTransporte.class, id);
    }

    public void update(long id, String material) throws MyEntityNotFoundException {
        EmbalagemDeTransporte embalagemDeTransporte = find(id);
        if (embalagemDeTransporte == null) {
            throw new MyEntityNotFoundException("Embalagem de transporte com id " + id + " não existe");
        }

        embalagemDeTransporte.setMaterial(material);
        entityManager.merge(embalagemDeTransporte);
    }

    public void remove(long id) throws MyEntityNotFoundException {
        EmbalagemDeTransporte embalagemDeTransporte = find(id);
        if (embalagemDeTransporte == null) {
            throw new MyEntityNotFoundException("Embalagem de transporte com id " + id + " não existe");
        }
        entityManager.remove(embalagemDeTransporte);
    }

    public List<EmbalagemDeTransporte> getAllEmbalagensDeTransporte() {
        return entityManager.createNamedQuery("getAllEmbalagensDeTransporte", EmbalagemDeTransporte.class).getResultList();
    }

    public void associateSensorToEmbalagem(long idEmbalagem, long idSensor) throws MyEntityNotFoundException {
        EmbalagemDeTransporte embalagemDeTransporte = find(idEmbalagem);
        if (embalagemDeTransporte == null) {
            throw new MyEntityNotFoundException("Embalagem with id " + idEmbalagem + " not found");
        }
        Sensor sensor = entityManager.find(Sensor.class, idSensor);
        if (sensor == null) {
            throw new MyEntityNotFoundException("Sensor with id " + idSensor + " not found");
        }
        embalagemDeTransporte.addSensor(sensor);
        entityManager.merge(embalagemDeTransporte);
    }

    public void removeSensorFromEmbalagem(long idEmbalagem, long idSensor) throws MyEntityNotFoundException {
        EmbalagemDeTransporte embalagemDeTransporte = find(idEmbalagem);
        if (embalagemDeTransporte == null) {
            throw new MyEntityNotFoundException("Embalagem with id " + idEmbalagem + " not found");
        }
        Sensor sensor = entityManager.find(Sensor.class, idSensor);
        if (sensor == null) {
            throw new MyEntityNotFoundException("Sensor with id " + idSensor + " not found");
        }
        embalagemDeTransporte.removeSensor(sensor);
        entityManager.merge(embalagemDeTransporte);
    }

    public void addEncomendaToEmbalagem(long idEmbalagem, long idEncomenda) throws MyEntityNotFoundException {
        EmbalagemDeTransporte embalagemDeTransporte = find(idEmbalagem);
        if (embalagemDeTransporte == null) {
            throw new MyEntityNotFoundException("Embalagem with id " + idEmbalagem + " not found");
        }
        Encomenda encomenda = entityManager.find(Encomenda.class, idEncomenda);
        if (encomenda == null) {
            throw new MyEntityNotFoundException("Encomenda with id " + idEncomenda + " not found");
        }
        encomenda.addEmbalagemTransporte(embalagemDeTransporte);
        entityManager.merge(embalagemDeTransporte);
    }


    public void removeEncomendaToEmbalagem(long idEmbalagem, long idEncomenda) throws MyEntityNotFoundException {
        EmbalagemDeTransporte embalagemDeTransporte = find(idEmbalagem);
        if (embalagemDeTransporte == null) {
            throw new MyEntityNotFoundException("Embalagem with id " + idEmbalagem + " not found");
        }
        Encomenda encomenda = entityManager.find(Encomenda.class, idEncomenda);
        if (encomenda == null) {
            throw new MyEntityNotFoundException("Encomenda with id " + idEncomenda + " not found");
        }
        encomenda.removeEmbalagemTransporte(embalagemDeTransporte);
        entityManager.merge(embalagemDeTransporte);
    }

    public EmbalagemDeTransporte getEmbalagensDeTransporteDetails(long id) {
        EmbalagemDeTransporte embalagemDeTransporte = find(id);
        if (embalagemDeTransporte != null) {
            Hibernate.initialize(embalagemDeTransporte.getSensores());
            Hibernate.initialize(embalagemDeTransporte.getEncomendas());
        }
        return embalagemDeTransporte;
    }
}
