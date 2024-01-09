package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Observacao;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Sensor;

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


    public Observacao find(Long id) {
        return entityManager.find(Observacao.class, id);
    }

    public List<Observacao> getAll() {
        return entityManager.createNamedQuery("getAllObservacao", Observacao.class).getResultList();
    }

    public void create(String valor, long sensorId) {

        Sensor sensor = entityManager.find(Sensor.class, sensorId);
        if (sensor == null) throw new IllegalArgumentException("Sensor with id " + sensorId + " not found.");

        Observacao observacao = new Observacao(getTimestamp(), valor, sensor);
        entityManager.persist(observacao);
    }

    public void update(long id, String valor, long sensorId) {
        Observacao observacao = find(id);

        if (sensorId == 0 && valor != null){
            sensorId = observacao.getSensor().getId();
        }

        if (valor == null && sensorId != 0){
            valor = observacao.getValor();
        }

        Sensor sensor = entityManager.find(Sensor.class, sensorId);
        if (sensor == null) throw new IllegalArgumentException("Sensor with id " + sensorId + " not found.");

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
        entityManager.remove(observacao);
    }

    public String getTimestamp(){
        LocalDateTime timestampToFormat = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestampToFormat.format(formatter);
    }

}
