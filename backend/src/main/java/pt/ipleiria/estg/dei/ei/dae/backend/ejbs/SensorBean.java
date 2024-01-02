package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Sensor;

import java.util.List;

@Stateless
public class SensorBean {
    @PersistenceContext
    private EntityManager entityManager;

    public void create(long idSensor, String tipo, String valor, String unidade) {
        Sensor sensor = new Sensor(idSensor, tipo, valor, unidade);
        entityManager.persist(sensor);
    }

    public Sensor find(long id) {
        return entityManager.find(Sensor.class, id);
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

    public void update(long id, long idSensor, String tipo, String unidade, String valor) {
        Sensor sensor = find(id);

        sensor.setIdSensor(idSensor);
        sensor.setTipo(tipo);
        sensor.setUnidade(unidade);
        sensor.setValor(valor);
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
