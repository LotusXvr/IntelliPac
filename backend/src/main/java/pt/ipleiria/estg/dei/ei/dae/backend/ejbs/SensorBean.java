package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Sensor;

import java.util.List;

@Stateless
public class SensorBean {
    @PersistenceContext
    private EntityManager entityManager;

    public void create(String tipo, String valor, String unidade) {
        Sensor sensor = new Sensor(tipo, valor, unidade);
        entityManager.persist(sensor);
    }

    public Sensor find(Long id) {
        return entityManager.find(Sensor.class, id);
    }

    public List<Sensor> getAll() {
        return entityManager.createNamedQuery("getAllSensor", Sensor.class).getResultList();
    }

    public void update(Sensor sensor) {
        entityManager.merge(sensor);
    }

    public void remove(Sensor sensor) {
        entityManager.remove(entityManager.contains(sensor) ? sensor : entityManager.merge(sensor));
    }

    // Outros métodos conforme necessário para operações relacionadas a SensorDispositivo
}
