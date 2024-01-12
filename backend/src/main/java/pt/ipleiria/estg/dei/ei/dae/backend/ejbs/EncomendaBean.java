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
            encomenda = new Encomenda(cliente,data, operadorDeLogistica, "pendente");
            entityManager.persist(encomenda);
        }
        catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
        cliente.addEncomenda(encomenda);
        operadorDeLogistica.addEncomenda(encomenda);

        return encomenda;
    }


    public Encomenda find(long id) {
        return entityManager.find(Encomenda.class, id);
    }

    public void update(long id, String estado)  throws MyEntityNotFoundException{
        Encomenda encomenda = find(id);
        if (encomenda == null) {
            throw new MyEntityNotFoundException("Encomenda com id " + id + " não existe");
        }

        encomenda.setEstado(estado);
        entityManager.merge(encomenda);
    }

    public void remove(long id) throws MyEntityNotFoundException {
        Encomenda encomenda = find(id);
        if (encomenda == null) {
            throw new MyEntityNotFoundException("Encomenda com id " + id + " não existe");
        }
        entityManager.remove(encomenda);
    }
    public List<Encomenda> getAllEncomendasCliente(String clienteUsername) {
        var cliente = clienteBean.find(clienteUsername);
        return entityManager.createNamedQuery("getAllEncomendasCliente", Encomenda.class).setParameter("cliente", cliente).getResultList();
    }
    public List<Encomenda> getAllEncomendasOperadoresLogistica(String operadorUsername) {
        var operadorLogistica = operadorDeLogisticaBean.find(operadorUsername);
        return entityManager.createNamedQuery("getAllEncomendasOperadoresLogistica", Encomenda.class).setParameter("operador", operadorLogistica).getResultList();
    }
}

