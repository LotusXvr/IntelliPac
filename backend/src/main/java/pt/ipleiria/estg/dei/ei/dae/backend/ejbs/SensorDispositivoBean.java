package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.SensorDispositivo;

@Stateless
public class SensorDispositivoBean {
    @PersistenceContext
    private EntityManager entityManager;

    public void createSensorDispositivo(SensorDispositivo sensorDispositivo) {
        entityManager.persist(sensorDispositivo);
    }

    public SensorDispositivo findSensorDispositivoById(Long id) {
        return entityManager.find(SensorDispositivo.class, id);
    }

    public void updateSensorDispositivo(SensorDispositivo sensorDispositivo) {
        entityManager.merge(sensorDispositivo);
    }

    public void removeSensorDispositivo(SensorDispositivo sensorDispositivo) {
        entityManager.remove(entityManager.contains(sensorDispositivo) ? sensorDispositivo : entityManager.merge(sensorDispositivo));
    }

    // Outros métodos conforme necessário para operações relacionadas a SensorDispositivo
}
