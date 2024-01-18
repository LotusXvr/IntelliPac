package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Sensor;

import java.util.List;

@Stateless
public class SensorBean {
    @EJB
    ObservacaoBean observacaoBean;
    @PersistenceContext
    private EntityManager entityManager;

    public Sensor create(String tipo, String unidade) {
        Long maxIdSensor = (Long) entityManager.createQuery("SELECT MAX(s.idSensor) FROM Sensor s").getSingleResult();

        long idSensor = (maxIdSensor != null) ? maxIdSensor + 1 : 1;
        Sensor sensor = new Sensor(idSensor, tipo, unidade, 0);
        entityManager.persist(sensor);
        return sensor;
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
        List<Sensor> sensores = entityManager.createNamedQuery("getAllSensor", Sensor.class).getResultList();
        for (Sensor sensor : sensores) {
            Hibernate.initialize(sensor.getObservacoes());
            Hibernate.initialize(sensor.getEmbalagens());
        }
        return sensores;
    }

    public Sensor findSensorDetails(long id) {

        Sensor sensor = entityManager.find(Sensor.class, id);
        if (sensor != null) {

            Hibernate.initialize(sensor.getObservacoes());
            Hibernate.initialize(sensor.getEmbalagens());
        }
        return sensor;

    }

    public void update(long id, long idSensor, String tipo, String unidade) {
        Sensor sensor = find(id);
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor with id " + id + " not found.");
        }

        sensor.setIdSensor(idSensor);
        sensor.setTipo(tipo);
        sensor.setUnidade(unidade);
        entityManager.merge(sensor);
    }

    public void remove(long id) {
        Sensor sensor = entityManager.find(Sensor.class, id);
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor with id " + id + " not found.");
        }
        entityManager.remove(sensor);
    }


    public void gerarObservacao(long sensorId) throws Exception {
        Sensor sensor = entityManager.find(Sensor.class, sensorId);
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor with id " + sensorId + " not found.");
        }

        // Nesta função teremos de ver primeiro qual é o tipo do sensor
        // Para depois poder gerar um valor aleatório dentro dos limites
        // Danificado > 0 ou 1
        // Temperatura > -50 a 200
        // Vento > 0 a 100
        // Humidade > 0 a 100
        // Aceleração > 0 a 100
        // Luminosidade > 0 a 100
        // Pressão > 0 a 100
        // Localização > 0 a 100
        String valor;
        if (sensor.getTipo().equals("Danificado")) {
            valor = String.valueOf(Math.round(Math.random()));
        } else if (sensor.getTipo().equals("Temperatura")) {
            valor = String.valueOf(Math.round(Math.random() * 250 - 50));
        } else {
            valor = String.valueOf(Math.round(Math.random() * 101));
        }

        observacaoBean.create(valor, sensorId);
    }
}
