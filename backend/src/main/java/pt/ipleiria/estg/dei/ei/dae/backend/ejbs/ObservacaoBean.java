package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.*;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

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

    @EJB
    private EmailBean emailBean;

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

        List<String> tipos = Arrays.asList("Vento", "Humidade", "Aceleração", "Luminosidade", "Pressão");
        double valorDouble = Double.parseDouble(valor);

        if (tipos.contains(sensor.getTipo()) && valorDouble > 85) {
            sendEmail(sensor, valor);
        }

        if (sensor.getTipo().equals("Danificado") && valor.equals("1")) {
            sendEmail(sensor, valor);

            if (checkTypeOfEmbalagem(sensor, sensor.getEmbalagens().size()) == 1) {
                EmbalagemDeTransporte embalagemDeTransporte = embalagemDeTransporteBean.find(sensor.getEmbalagens().get(sensor.getEmbalagens().size() - 1).getId());
                for (Encomenda encomenda : embalagemDeTransporte.getEncomendas()) {
                    encomendaBean.patchEstado(encomenda.getId(), "Danificada");
                }
            }
        }

        if (sensor.getTipo().equals("Temperatura") && (valorDouble < -10 || valorDouble > 80)) {
            sendEmail(sensor, valor);
        }

        Observacao observacao = new Observacao(getTimestamp(), valor, sensor);
        sensor.addObservacao(observacao);

        entityManager.persist(observacao);
    }

    public void sendEmail(Sensor sensor, String valor) {
        int numeroEmbalagens = sensor.getEmbalagens().size();
        int embalagem = checkTypeOfEmbalagem(sensor, numeroEmbalagens);

        if (embalagem == 2) {
            EmbalagemDeProduto embalagemProdutoSensor = embalagemDeProdutoBean.find(sensor.getEmbalagens().get(numeroEmbalagens - 1).getId());

            ProdutoFisico produto = embalagemProdutoSensor.getProdutos().get(embalagemProdutoSensor.getProdutos().size() - 1);
            String emailConsumidor = produto.getEncomenda().getConsumidorFinal().getEmail();
            String emailOperadorLogistica = produto.getEncomenda().getOperadorLogistica().getEmail();

            if (!sensor.getTipo().equals("Danificado")) {
                emailBean.send(emailConsumidor, "Encomenda " + produto.getEncomenda().getId() + " possui um produto com " + sensor.getTipo() + " fora do intervalo", "A encomenda " + produto.getEncomenda().getId() + " possui o produto " + produto.getNomeProduto() + " com " + sensor.getTipo() + " fora do intervalo favoravel" + " (" + valor + " " + sensor.getUnidade() + ")");
                emailBean.send(emailOperadorLogistica, "Encomenda " + produto.getEncomenda().getId() + " possui um produto com " + sensor.getTipo() + " fora do intervalo", "A encomenda " + produto.getEncomenda().getId() + " possui o produto " + produto.getNomeProduto() + " com " + sensor.getTipo() + " fora do intervalo favoravel" + " (" + valor + " " + sensor.getUnidade() + ")");
            } else {
                emailBean.send(emailConsumidor, "Encomenda " + produto.getEncomenda().getId() + " possui um produto danificado", "A encomenda " + produto.getEncomenda().getId() + " possui o produto " + produto.getNomeProduto() + " danificado");
                emailBean.send(emailOperadorLogistica, "Encomenda " + produto.getEncomenda().getId() + " possui um produto danificado", "A encomenda " + produto.getEncomenda().getId() + " possui o produto " + produto.getNomeProduto() + " danificado");
            }

            return;
        }

        if (embalagem == 1) {
            EmbalagemDeTransporte embalagemDeTransporte = embalagemDeTransporteBean.find(sensor.getEmbalagens().get(numeroEmbalagens - 1).getId());
            for (Encomenda encomenda : embalagemDeTransporte.getEncomendas()) {
                // Em caso de embalagem de transporte, altera-se o estado da encomenda para "Danificada"
                // e notifica-se ambos o consumidor e o operador de logistica
                if (!sensor.getTipo().equals("Danificado")) {
                    emailBean.send(encomenda.getConsumidorFinal().getEmail(), "Encomenda " + encomenda.getId() + " possui um produto com " + sensor.getTipo() + " fora do intervalo", "A encomenda " + encomenda.getId() + " possui a embalagem " + embalagemDeTransporte.getMaterial() + " com " + sensor.getTipo() + " fora do intervalo favoravel" + " (" + valor + " " + sensor.getUnidade() + ")");
                    emailBean.send(encomenda.getOperadorLogistica().getEmail(), "Encomenda " + encomenda.getId() + " possui um produto com " + sensor.getTipo() + " fora do intervalo", "A encomenda " + encomenda.getId() + " possui a embalagem " + embalagemDeTransporte.getMaterial() + " com " + sensor.getTipo() + " fora do intervalo favoravel" + " (" + valor + " " + sensor.getUnidade() + ")");
                } else {

                    emailBean.send(encomenda.getConsumidorFinal().getEmail(), "Encomenda " + encomenda.getId() + " está com a embalagem " + embalagemDeTransporte.getMaterial() + "danificada", "A encomenda " + encomenda.getId() + " está com a embalagem " + embalagemDeTransporte.getMaterial() + " danificada, ficando assim a sua encomenda também danificada");
                    emailBean.send(encomenda.getOperadorLogistica().getEmail(), "Encomenda " + encomenda.getId() + " está com a embalagem " + embalagemDeTransporte.getMaterial() + "danificada", "A encomenda " + encomenda.getId() + " está com a embalagem " + embalagemDeTransporte.getMaterial() + " danificada, ficando assim a sua encomenda também danificada");
                }
            }
        }
    }

    public int checkTypeOfEmbalagem(Sensor sensor, int numeroEmbalagens) {

        if (numeroEmbalagens == 0) {
            throw new IllegalArgumentException("Sensor with id " + sensor.getIdSensor() + " does not have any embalagem associated.");
        }

        Embalagem embalagemDoSensor = sensor.getEmbalagens().get(numeroEmbalagens - 1);
        EmbalagemDeTransporte embalagemDeTransporte = embalagemDeTransporteBean.find(embalagemDoSensor.getId());
        EmbalagemDeProduto embalagemDeProduto = null;

        if (embalagemDeTransporte != null) {
            // Embalagem TRANSPORTE
            return 1;
        }

        embalagemDeProduto = embalagemDeProdutoBean.find(embalagemDoSensor.getId());

        if (embalagemDeProduto == null)
            throw new IllegalArgumentException("Embalagem with id " + sensor.getEmbalagens().get(numeroEmbalagens - 1).getId() + " not found.");

        // Embalagem PRODUTO
        return 2;
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
