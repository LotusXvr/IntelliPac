package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Observacao;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Sensor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class ObservacaoBean {

    @PersistenceContext
    private EntityManager entityManager;


    public Observacao find(Long id) {
        return entityManager.find(Observacao.class, id);
    }

    public List<Observacao> getAll() {
        return entityManager.createNamedQuery("getAllObservacao", Observacao.class).getResultList();
    }

    public void create(String timestamp, String valor, long sensorId) {

        Sensor sensor = entityManager.find(Sensor.class, sensorId);
        if (sensor == null) throw new IllegalArgumentException("Sensor with id " + sensorId + " not found.");

        Observacao observacao = new Observacao(timestamp, valor, sensor);
        entityManager.persist(observacao);
    }

    public void update(Observacao observacao) {
        entityManager.merge(observacao);
    }

    public void remove(Observacao observacao) {
        entityManager.remove(entityManager.contains(observacao) ? observacao : entityManager.merge(observacao));
    }


}
