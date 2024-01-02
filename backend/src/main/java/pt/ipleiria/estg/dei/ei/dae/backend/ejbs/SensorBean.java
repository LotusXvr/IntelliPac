package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Sensor;

@Stateless
public class SensorBean {
    @PersistenceContext
    private EntityManager entityManager;

    public void createSensor(Sensor sensor) {
        entityManager.persist(sensor);
    }

    public Sensor findSensorById(Long id) {
        return entityManager.find(Sensor.class, id);
    }

    public void updateSensor(Sensor sensor) {
        entityManager.merge(sensor);
    }

    public void removeSensor(Sensor sensor) {
        entityManager.remove(entityManager.contains(sensor) ? sensor : entityManager.merge(sensor));
    }

    // Outros métodos conforme necessário para operações relacionadas a SensorDispositivo
}
