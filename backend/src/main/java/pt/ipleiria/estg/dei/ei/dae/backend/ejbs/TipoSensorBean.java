package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.TipoEmbalagemProduto;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.TipoSensor;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;
@Stateless
public class TipoSensorBean {

    @PersistenceContext
    private EntityManager entityManager;


    public boolean exists(String tipo, String unidade) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(s.tipo) FROM TipoSensor s WHERE s.tipo = :tipo AND s.unidade = :unidade",
                Long.class
        );
        query.setParameter("tipo", tipo);
        query.setParameter("unidade", unidade);
        return (Long)query.getSingleResult() > 0L;
    }

    public TipoSensor create(String tipo, String unidade) throws Exception {

        if(exists(tipo, unidade)) {
            throw new MyEntityExistsException("Tipo Sensor com tipo " + tipo + " e unidade"+ unidade +" já existe");
        }

        TipoSensor tipoSensor = null;

        try {
            tipoSensor = new TipoSensor(tipo, unidade);
            entityManager.persist(tipoSensor);
        }
        catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
        return tipoSensor;
    }

    public TipoSensor find(long id) {
        return entityManager.find(TipoSensor.class, id);
    }

    public void update(long id, String tipo, String unidade) throws MyEntityNotFoundException, MyEntityExistsException {
        TipoSensor tipoSensor = find(id);
        if (tipoSensor == null) {
            throw new MyEntityNotFoundException("Tipo Sensor com id " + id + " não existe");
        }

        if(exists(tipo, unidade)) {
            throw new MyEntityExistsException("Tipo Sensor com tipo " + tipo + " e unidade "+ unidade +" já existe");
        }
        tipoSensor.setTipo(tipo);
        tipoSensor.setUnidade(unidade);

        entityManager.merge(tipoSensor);
    }

    public void remove(long id) throws MyEntityNotFoundException {
        TipoSensor tipoSensor = find(id);
        if (tipoSensor == null) {
            throw new MyEntityNotFoundException("Tipo Sensor com id " + id + " não existe");
        }
        entityManager.remove(tipoSensor);
    }

    public List<TipoSensor> getAllTipoSensor() {
        return entityManager.createNamedQuery("getAllTipoSensor", TipoSensor.class).getResultList();
    }
}
