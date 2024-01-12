package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EncomendaDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoFisicoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.*;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public Encomenda create(EncomendaDTO encomendaDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {

        var cliente = clienteBean.find(encomendaDTO.getConsumidorFinal());
        if (cliente == null) {
            throw new MyEntityNotFoundException("Cliente com id " + cliente + " não existe");
        }

        var operadorDeLogistica = operadorDeLogisticaBean.find(encomendaDTO.getOperadorLogistica());
        if (operadorDeLogistica == null) {
            throw new MyEntityNotFoundException("Operador com id " + cliente + " não existe");
        }

        Encomenda encomenda = null;

        try {
            encomenda = new Encomenda(cliente, getTimestamp(), operadorDeLogistica, "pendente");
            ProdutoFisico produtoFisico = null;
            // Adicione lógica para associar os produtosCatalogo à encomenda
            for (ProdutoFisicoDTO produtoDTO : encomendaDTO.getProdutos()) {
                ProdutoCatalogo produtoCatalogo = entityManager.find(ProdutoCatalogo.class, produtoDTO.getId());

                        // Agora, em vez de usar o DTO diretamente, crie uma instância de ProdutoFisico e persista-a
                        produtoFisico = new ProdutoFisico(produtoCatalogo, encomenda);
                entityManager.persist(produtoFisico);

                // Adicione o produtoFisico à encomenda
                encomenda.addProduto(produtoFisico);
            }

            entityManager.persist(encomenda);
        } catch (ConstraintViolationException e) {
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

    public List<Encomenda> getAllEncomendas() {
        List<Encomenda> encomendas = entityManager.createQuery("SELECT e FROM Encomenda e", Encomenda.class)
                .getResultList();

        for (Encomenda encomenda : encomendas) {
            Hibernate.initialize(encomenda.getProdutos());
        }

        return encomendas;
    }

    public String getTimestamp() {
        LocalDateTime timestampToFormat = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return timestampToFormat.format(formatter);
    }

}

