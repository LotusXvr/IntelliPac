package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.*;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.Date;
import java.util.List;

@Stateless
public class EncomendaBean {
    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private ClienteBean clienteBean;

    @EJB
    private OperadorDeLogisticaBean operadorDeLogisticaBean;

    public Encomenda create(String consumidorFinal, String operadorLogistica) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {

        var cliente = clienteBean.find(consumidorFinal);
        if (cliente == null) {
            throw new MyEntityNotFoundException("Cliente com id " + cliente + " não existe");
        }

        var operadorDeLogistica = operadorDeLogisticaBean.find(operadorLogistica);
        if (operadorDeLogistica == null) {
            throw new MyEntityNotFoundException("Operador com id " + cliente + " não existe");
        }

        Encomenda encomenda = null;

        Date data = new Date();
        try{
            encomenda = new Encomenda(cliente,data, operadorDeLogistica);
            entityManager.persist(encomenda);
        }
        catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
        cliente.addEncomenda(encomenda);
        operadorDeLogistica.addEncomenda(encomenda);

        return encomenda;
    }


    public Encomenda findEncomendaById(Long id) {
        return entityManager.find(Encomenda.class, id);
    }

    public void updateEncomenda(Encomenda encomenda) {
        entityManager.merge(encomenda);
    }

    public void removeEncomenda(Encomenda encomenda) {
        entityManager.remove(entityManager.contains(encomenda) ? encomenda : entityManager.merge(encomenda));
    }
}
