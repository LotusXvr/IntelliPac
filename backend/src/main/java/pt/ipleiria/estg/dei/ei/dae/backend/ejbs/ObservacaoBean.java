package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.*;
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

    @EJB
    private TipoSensorBean tipoSensorBean;

    @EJB
    private EmbalagemDeTransporteBean embalagemDeTransporteBean;

    @EJB
    private EmbalagemDeProdutoBean embalagemDeProdutoBean;

    @EJB
    private EncomendaBean encomendaBean;

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

    public void create(String valor, long sensorId) throws Exception {

        Sensor sensor = entityManager.find(Sensor.class, sensorId);
        if (sensor == null) throw new IllegalArgumentException("Sensor with id " + sensorId + " not found.");

        // verificar se o sensor está associado a uma embalagem de transporte ou de produto
        int numeroEmbalagens = sensor.getEmbalagens().size(); // a ultima embalagem associada ao sensor

        if (numeroEmbalagens > 0)
        {
            Embalagem embalagemDoSensor = sensor.getEmbalagens().get(numeroEmbalagens - 1);
            EmbalagemDeTransporte embalagemDeTransporte = embalagemDeTransporteBean.find( embalagemDoSensor.getId() );
            EmbalagemDeProduto embalagemDeProduto = null;
            if (embalagemDeTransporte == null){
                embalagemDeProduto = embalagemDeProdutoBean.find(embalagemDoSensor.getId());
            }

            if (embalagemDeTransporte == null && embalagemDeProduto == null)
                throw new IllegalArgumentException("Embalagem with id " + sensor.getEmbalagens().get(numeroEmbalagens-1).getId() + " not found.");

            if (sensor.getTipo().equals("Danificado") && (valor.equals("1") || valor.equals("true")) ) {
                System.out.println("333: nigga entraste");
                Encomenda encomenda = embalagemDeTransporte.getEncomendas().get(embalagemDeTransporte.getEncomendas().size() - 1);

                encomendaBean.patchEstado(encomenda.getId(), "Danificada");
            }
        }

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
