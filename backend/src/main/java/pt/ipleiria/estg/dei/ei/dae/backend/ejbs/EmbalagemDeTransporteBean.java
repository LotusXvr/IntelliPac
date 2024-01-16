package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.*;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class EmbalagemDeTransporteBean {
    @PersistenceContext
    private EntityManager entityManager;

    public EmbalagemDeTransporte create(String material) {
        EmbalagemDeTransporte embalagemDeTransporte = new EmbalagemDeTransporte(material);
        entityManager.persist(embalagemDeTransporte);
        return embalagemDeTransporte;
    }

    public EmbalagemDeTransporte find(long id) {
        return entityManager.find(EmbalagemDeTransporte.class, id);
    }

    public void update(long id, String material) throws MyEntityNotFoundException {
        EmbalagemDeTransporte embalagemDeTransporte = find(id);
        if (embalagemDeTransporte == null) {
            throw new MyEntityNotFoundException("Embalagem de transporte com id " + id + " não existe");
        }

        embalagemDeTransporte.setMaterial(material);
        entityManager.merge(embalagemDeTransporte);
    }

    public void remove(long id) throws MyEntityNotFoundException {
        EmbalagemDeTransporte embalagemDeTransporte = find(id);
        if (embalagemDeTransporte == null) {
            throw new MyEntityNotFoundException("Embalagem de transporte com id " + id + " não existe");
        }
        entityManager.remove(embalagemDeTransporte);
    }

    public List<EmbalagemDeTransporte> getAllEmbalagensDeTransporte() {
        return entityManager.createNamedQuery("getAllEmbalagensDeTransporte", EmbalagemDeTransporte.class).getResultList();
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
        for (Sensor sensorTipo: embalagemDeTransporte.getSensores()
             ) {
            if(sensor.getTipo().compareTo(sensorTipo.getTipo()) == 0){
                throw new Exception("Embalagem já tem um sensor do mesmo tipo");
            }
        }
        if(sensor.getEstado() == 0){
            embalagemDeTransporte.addSensor(sensor);
            sensor.setEstado(1);
            entityManager.merge(embalagemDeTransporte);
        }else{
            if(sensor.getEstado() == 1){
                throw new Exception("Sensor está em uso na encomenda "+ sensor.getEmbalagens().get(sensor.getEmbalagens().size() - 1).getId());
            }
            if(sensor.getEstado() == 2){
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
        if(embalagemDeTransporte.getSensores().contains(sensor)){
            embalagemDeTransporte.removeSensor(sensor);
            sensor.setEstado(0);
            entityManager.merge(embalagemDeTransporte);
        }else{
            throw new Exception("O sensor "+ sensor.getId() +" não está associado à embalagem de transporte "+ embalagemDeTransporte.getId());
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
