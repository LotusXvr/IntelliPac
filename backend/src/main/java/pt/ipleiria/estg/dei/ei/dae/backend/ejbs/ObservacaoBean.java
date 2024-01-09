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

    public void create(String timestamp, String valor, long sensorId) {

        Sensor sensor = entityManager.find(Sensor.class, sensorId);
        if (sensor == null) throw new IllegalArgumentException("Sensor with id " + sensorId + " not found.");

        if (!isValidFormat(timestamp)) throw new IllegalArgumentException("Invalid timestamp format (Should be dd-mm-yyyy hh:mm:ss).");


        Observacao observacao = new Observacao(timestamp, valor, sensor);
        entityManager.persist(observacao);
    }

    public void update(Observacao observacao) {
        entityManager.merge(observacao);
    }

    public void remove(Observacao observacao) {
        entityManager.remove(entityManager.contains(observacao) ? observacao : entityManager.merge(observacao));
    }

    public static boolean isValidFormat(String timestamp) {
        LocalDateTime ldt = null;
        String format = "dd-MM-yyyy HH:mm:ss";
        Locale locale = Locale.ENGLISH;
        DateTimeFormatter fomatter = DateTimeFormatter.ofPattern(format, locale);

        try {
            ldt = LocalDateTime.parse(timestamp, fomatter);
            String result = ldt.format(fomatter);
            return result.equals(timestamp);
        } catch (DateTimeParseException e) {
            try {
                LocalDate ld = LocalDate.parse(timestamp, fomatter);
                String result = ld.format(fomatter);
                return result.equals(timestamp);
            } catch (DateTimeParseException exp) {
                try {
                    LocalTime lt = LocalTime.parse(timestamp, fomatter);
                    String result = lt.format(fomatter);
                    return result.equals(timestamp);
                } catch (DateTimeParseException e2) {
                    // Debugging purposes
                    //e2.printStackTrace();
                }
            }
        }

        return false;
    }

}
