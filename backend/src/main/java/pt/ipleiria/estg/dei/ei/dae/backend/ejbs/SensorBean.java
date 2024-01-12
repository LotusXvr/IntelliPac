package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.core.Response;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class SensorBean {
    @PersistenceContext
    private EntityManager entityManager;

    public void create(long idSensor, String tipo, String unidade, long idEmbalagem) {
        Sensor sensor = new Sensor(idSensor, tipo, unidade, idEmbalagem);
        entityManager.persist(sensor);
    }

    public Sensor find(long id) {
        Sensor sensor = entityManager.find(Sensor.class, id);
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor with id " + id + " not found.");
        }
        return sensor;
    }

    public Sensor findBySensorId(long idSensor) {
        // Use a TypedQuery with JPQL to find a Sensor based on idSensor
        List<Sensor> results = entityManager.createQuery(
                        "SELECT s FROM Sensor s WHERE s.idSensor = :idSensor", Sensor.class)
                .setParameter("idSensor", idSensor)
                .getResultList();

        if (results.isEmpty()) {
            return null;
        }

        return results.get(0);
    }

    public List<Sensor> getAll() {
        return entityManager.createNamedQuery("getAllSensor", Sensor.class).getResultList();
    }

    public Sensor findSensorWithObservacoes(long id) {
        Sensor sensor = entityManager.find(Sensor.class, id);
        if (sensor != null) {
            Hibernate.initialize(sensor.getObservacoes());
        }

        return sensor;
    }

    public void update(long id, long idSensor, String tipo, String unidade, long idEmbalagem) {
        Sensor sensor = find(id);
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor with id " + id + " not found.");
        }

        sensor.setIdSensor(idSensor);
        sensor.setTipo(tipo);
        sensor.setUnidade(unidade);
        sensor.setIdEmbalagem(idEmbalagem);
        entityManager.merge(sensor);
    }

    public void remove(long id) {
        Sensor sensor = entityManager.find(Sensor.class, id);
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor with id " + id + " not found.");
        }
        entityManager.remove(sensor);
    }

    // Outros métodos conforme necessário para operações relacionadas a SensorDispositivo
}
