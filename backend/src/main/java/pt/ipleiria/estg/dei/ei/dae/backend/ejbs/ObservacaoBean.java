package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Observacao;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

@Stateless
public class ObservacaoBean {

    @PersistenceContext
    private EntityManager entityManager;


    public Observacao find(Long id) throws MyEntityNotFoundException {
        Observacao observacao = entityManager.find(Observacao.class, id);
        if (observacao == null) {
            throw new MyEntityNotFoundException("Observacao with id " + id + " not found.");
        }
        return observacao;
    }

    public List<Observacao> getAll() {
        return entityManager.createNamedQuery("getAllObservacao", Observacao.class).getResultList();
    }

    public void create(String valor, long sensorId) {

        Sensor sensor = entityManager.find(Sensor.class, sensorId);
        if (sensor == null) throw new IllegalArgumentException("Sensor with id " + sensorId + " not found.");

        Observacao observacao = new Observacao(getTimestamp(), valor, sensor);
        sensor.addObservacao(observacao);

        entityManager.persist(observacao);
    }

    public void update(long id, String valor, long sensorId) throws MyEntityNotFoundException {
        Observacao observacao = find(id);

        if (observacao == null) throw new MyEntityNotFoundException("Observacao with id " + id + " not found.");

        // apenas o valor foi fornecido
        if (sensorId == 0 && valor != null) {
            sensorId = observacao.getSensor().getId();
        }

        // apenas o sensor foi fornecido
        if (valor == null && sensorId != 0) {
            valor = observacao.getValor();
        }

        Sensor sensor = entityManager.find(Sensor.class, sensorId);
        if (sensor == null) throw new MyEntityNotFoundException("Sensor with id " + sensorId + " not found.");

        // se o sensor tiver sido alterado, alterar a associaçao com o sensor novo
        if (observacao.getSensor().getId() != sensorId) {
            observacao.getSensor().removeObservacao(observacao);
            sensor.addObservacao(observacao);
        }

        observacao.setTimestamp(getTimestamp());
        observacao.setValor(valor);
        observacao.setSensor(sensor);
        entityManager.merge(observacao);
    }

    public void remove(long id) {
        Observacao observacao = entityManager.find(Observacao.class, id);
        if (observacao == null) {
            throw new IllegalArgumentException("Observacao with id " + id + " not found.");
        }
        observacao.getSensor().removeObservacao(observacao);
        entityManager.remove(observacao);
    }

    public List<Observacao> getObservacoesBySensor(long sensorId) throws MyEntityNotFoundException {
        Sensor sensor = entityManager.find(Sensor.class, sensorId);
        if (sensor == null) throw new MyEntityNotFoundException("Sensor with id " + sensorId + " not found.");
        return entityManager.createNamedQuery("getObservacoesBySensor", Observacao.class).setParameter("id", sensorId).getResultList();
    }

    public Observacao getLastObservacaoBySensor(long sensorId) throws MyEntityNotFoundException {
        Sensor sensor = entityManager.find(Sensor.class, sensorId);
        if (sensor == null) throw new MyEntityNotFoundException("Sensor with id " + sensorId + " not found.");
        return entityManager.createNamedQuery("getObservacoesBySensor", Observacao.class).setParameter("id", sensorId).setMaxResults(1).getSingleResult();
    }


    // Utility method
    public String getTimestamp() {
        LocalDateTime timestampToFormat = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return timestampToFormat.format(formatter);
    }
}
