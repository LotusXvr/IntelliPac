package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.EmbalagemDeTransporte;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class EmbalagemDeTransporteBean {
    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private EncomendaBean encomendaBean;

    @EJB
    private SensorBean sensorBean;

    public EmbalagemDeTransporte create(String material, long altura, long largura, long comprimento, int estado) {
        EmbalagemDeTransporte embalagemDeTransporte = new EmbalagemDeTransporte(material, altura, largura, comprimento, estado);
        entityManager.persist(embalagemDeTransporte);

        Sensor sensor = sensorBean.create("Localização", "GPS");
        sensor.setEstado(1);
        embalagemDeTransporte.addSensor(sensor);

        return embalagemDeTransporte;
    }

    public EmbalagemDeTransporte find(long id) {
        return entityManager.find(EmbalagemDeTransporte.class, id);
    }

    public void update(long id, String material, long altura, long largura, long comprimento, int estado) throws MyEntityNotFoundException {
        EmbalagemDeTransporte embalagemDeTransporte = find(id);
        if (embalagemDeTransporte == null) {
            throw new MyEntityNotFoundException("Embalagem de transporte com id " + id + " não existe");
        }

        embalagemDeTransporte.setMaterial(material);
        embalagemDeTransporte.setAltura(altura);
        embalagemDeTransporte.setLargura(largura);
        embalagemDeTransporte.setComprimento(comprimento);
        embalagemDeTransporte.setEstado(estado);
        entityManager.merge(embalagemDeTransporte);
    }

    public void remove(long id) throws MyEntityNotFoundException {
        EmbalagemDeTransporte embalagemDeTransporte = find(id);
        if (embalagemDeTransporte == null) {
            throw new MyEntityNotFoundException("Embalagem de transporte com id " + id + " não existe");
        }
        if(embalagemDeTransporte.getEncomendas().size() != 0){
            throw new MyEntityNotFoundException("Embalagem de transporte com o username " + id + " não pode ser eliminado pois tem encomendas");
        }
        entityManager.remove(embalagemDeTransporte);
    }

    public List<EmbalagemDeTransporte> getAllEmbalagensDeTransporte() {
        List<EmbalagemDeTransporte> embalagensTransporte = entityManager.createNamedQuery("getAllEmbalagensDeTransporte", EmbalagemDeTransporte.class).getResultList();
        for (EmbalagemDeTransporte embalagemDeTransporte : embalagensTransporte) {
            Hibernate.initialize(embalagemDeTransporte.getSensores());
            Hibernate.initialize(embalagemDeTransporte.getEncomendas());
        }
        return embalagensTransporte;
    }

    public void associateSensorToEmbalagem(long idEmbalagem, long idSensor) throws Exception {
        EmbalagemDeTransporte embalagemDeTransporte = find(idEmbalagem);
        if (embalagemDeTransporte == null) {
            throw new MyEntityNotFoundException("Embalagem with id " + idEmbalagem + " not found");
        }
        Sensor sensor = entityManager.find(Sensor.class, idSensor);
        if (sensor == null) {
            throw new MyEntityNotFoundException("Sensor with id " + idSensor + " not found");
        }
        for (Sensor sensorTipo : embalagemDeTransporte.getSensores()
        ) {
            if (sensor.getTipo().compareTo(sensorTipo.getTipo()) == 0) {
                throw new Exception("Embalagem já tem um sensor do mesmo tipo");
            }
        }
        if (sensor.getEstado() == 0) {
            embalagemDeTransporte.addSensor(sensor);
            sensor.setEstado(1);
            entityManager.merge(embalagemDeTransporte);
        } else {
            if (sensor.getEstado() == 1) {
                throw new Exception("Sensor está em uso na encomenda " + sensor.getEmbalagens().get(sensor.getEmbalagens().size() - 1).getId());
            }
            if (sensor.getEstado() == 2) {
                throw new Exception("O sensor está associado a uma embalagem produto pelo que não pode ser reutilizado");
            }
        }
    }

    public void removeSensorFromEmbalagem(long idEmbalagem, long idSensor) throws Exception {
        EmbalagemDeTransporte embalagemDeTransporte = find(idEmbalagem);
        if (embalagemDeTransporte == null) {
            throw new MyEntityNotFoundException("Embalagem with id " + idEmbalagem + " not found");
        }
        Sensor sensor = entityManager.find(Sensor.class, idSensor);
        if (sensor == null) {
            throw new MyEntityNotFoundException("Sensor with id " + idSensor + " not found");
        }
        if (embalagemDeTransporte.getSensores().contains(sensor)) {
            embalagemDeTransporte.removeSensor(sensor);
            sensor.setEstado(0);
            entityManager.merge(embalagemDeTransporte);
        } else {
            throw new Exception("O sensor " + sensor.getId() + " não está associado à embalagem de transporte " + embalagemDeTransporte.getId());
        }

    }

    public void addEncomendaToEmbalagem(long idEmbalagem, long idEncomenda) throws MyEntityNotFoundException {
        EmbalagemDeTransporte embalagemDeTransporte = find(idEmbalagem);
        if (embalagemDeTransporte == null) {
            throw new MyEntityNotFoundException("Embalagem with id " + idEmbalagem + " not found");
        }
        Encomenda encomenda = entityManager.find(Encomenda.class, idEncomenda);
        if (encomenda == null) {
            throw new MyEntityNotFoundException("Encomenda with id " + idEncomenda + " not found");
        }
        encomenda.addEmbalagemTransporte(embalagemDeTransporte);
        entityManager.merge(embalagemDeTransporte);
    }

    public void patchEstado(long id, int estado) throws Exception {
        EmbalagemDeTransporte embalagemDeTransporte = find(id);
        if (embalagemDeTransporte == null) {
            throw new MyEntityNotFoundException("Embalagem de transporte com id " + id + " não existe");
        }
        embalagemDeTransporte.setEstado(estado);

        if (estado == 1) {
            for (Encomenda encomenda : embalagemDeTransporte.getEncomendas()) {
                encomendaBean.patchEstado(encomenda.getId(), "TRANSPORTE");
            }
        }

        entityManager.merge(embalagemDeTransporte);
    }

    public void removeEncomendaToEmbalagem(long idEmbalagem, long idEncomenda) throws MyEntityNotFoundException {
        EmbalagemDeTransporte embalagemDeTransporte = find(idEmbalagem);
        if (embalagemDeTransporte == null) {
            throw new MyEntityNotFoundException("Embalagem with id " + idEmbalagem + " not found");
        }
        Encomenda encomenda = entityManager.find(Encomenda.class, idEncomenda);
        if (encomenda == null) {
            throw new MyEntityNotFoundException("Encomenda with id " + idEncomenda + " not found");
        }

        encomenda.removeEmbalagemTransporte(embalagemDeTransporte);

        if (embalagemDeTransporte.getEncomendas().size() - 1 <= 0) {
            embalagemDeTransporte.setEstado(0);
        }

        entityManager.merge(embalagemDeTransporte);
    }

    public EmbalagemDeTransporte getEmbalagensDeTransporteDetails(long id) {
        EmbalagemDeTransporte embalagemDeTransporte = find(id);
        if (embalagemDeTransporte != null) {
            Hibernate.initialize(embalagemDeTransporte.getSensores());
            Hibernate.initialize(embalagemDeTransporte.getEncomendas());
        }
        return embalagemDeTransporte;
    }
}
