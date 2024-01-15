package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EncomendaDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoFisicoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.FabricanteDeProdutos;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.ProdutoCatalogo;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.ProdutoFisico;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Stateless
public class EncomendaBean {
    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private ClienteBean clienteBean;

    @EJB
    private OperadorDeLogisticaBean operadorDeLogisticaBean;

    @EJB
    private EmailBean emailBean;

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
                ProdutoCatalogo produtoCatalogo = entityManager.find(ProdutoCatalogo.class, produtoDTO.getProdutoCatalogoId());
                if (produtoCatalogo == null) {
                    throw new MyEntityNotFoundException("ProdutoCatalogo com id " + produtoDTO.getProdutoCatalogoId() + " não existe");
                }

                FabricanteDeProdutos fabricanteDeProdutos = entityManager.find(FabricanteDeProdutos.class, produtoCatalogo.getFabricante().getUsername());

                // Agora, em vez de usar o DTO diretamente, crie uma instância de ProdutoFisico e persista-a
                produtoFisico = new ProdutoFisico(produtoCatalogo.getNomeProduto(), fabricanteDeProdutos, produtoCatalogo, encomenda);
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

    public Encomenda update(long id, String estado) throws MyEntityNotFoundException {
        Encomenda encomenda = find(id);
        if (encomenda == null) {
            throw new MyEntityNotFoundException("Encomenda com id " + id + " não existe");
        }

        encomenda.setEstado(estado);
        entityManager.merge(encomenda);
        return encomenda;
    }

    public Encomenda remove(long id) throws MyEntityNotFoundException {
        Encomenda encomenda = find(id);
        if (encomenda == null) {
            throw new MyEntityNotFoundException("Encomenda com id " + id + " não existe");
        }
        entityManager.remove(encomenda);
        return encomenda;
    }

    public List<Encomenda> getAllEncomendasCliente(String clienteUsername) {
        var cliente = clienteBean.find(clienteUsername);

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente com username " + clienteUsername + " não existe");
        }

        List<Encomenda> encomendas = entityManager.createNamedQuery("getAllEncomendasCliente", Encomenda.class).setParameter("cliente", cliente).getResultList();

        for (Encomenda encomenda : encomendas) {
            Hibernate.initialize(encomenda.getProdutos());
            Hibernate.initialize(encomenda.getEmbalagensTransporte());
        }

        return encomendas;
    }

    public List<Encomenda> getAllEncomendasOperadoresLogistica(String operadorUsername) {
        var operadorLogistica = operadorDeLogisticaBean.find(operadorUsername);

        if (operadorLogistica == null) {
            throw new IllegalArgumentException("Operador de logistica com username " + operadorUsername + " não existe");
        }

        List<Encomenda> encomendas = entityManager.createNamedQuery("getAllEncomendasOperadoresLogistica", Encomenda.class).setParameter("operador", operadorLogistica).getResultList();

        for (Encomenda encomenda : encomendas) {
            Hibernate.initialize(encomenda.getProdutos());
            Hibernate.initialize(encomenda.getEmbalagensTransporte());
        }

        return encomendas;
    }

    public List<Encomenda> getAllEncomendas() {
        List<Encomenda> encomendas = entityManager.createQuery("SELECT e FROM Encomenda e", Encomenda.class)
                .getResultList();

        for (Encomenda encomenda : encomendas) {
            Hibernate.initialize(encomenda.getProdutos());
            Hibernate.initialize(encomenda.getEmbalagensTransporte());
        }

        return encomendas;
    }

    public String getTimestamp() {
        LocalDateTime timestampToFormat = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return timestampToFormat.format(formatter);
    }

    public Encomenda getEncomendaById(long id) {
        Encomenda encomenda = entityManager.find(Encomenda.class, id);
        if (encomenda != null) {
            Hibernate.initialize(encomenda.getProdutos());
            Hibernate.initialize(encomenda.getEmbalagensTransporte());
        }
        return encomenda;

    }

    public List<Encomenda> getEncomendasByEstado(String estado) {
        if (estado == null) {
            throw new IllegalArgumentException("Estado não pode ser null");
        }

        estado = estado.toLowerCase();
        // verificar se estado corresponde a um dos estados possíveis
        if (!estadoValido(estado)) {
            throw new IllegalArgumentException("Estado inválido (Estado tem de ser pendente, processamento, transporte ou entregue)");
        }

        List<Encomenda> encomendas = entityManager.createNamedQuery("getEncomendasByEstado", Encomenda.class).setParameter("estado", estado).getResultList();

        for (Encomenda encomenda : encomendas) {
            Hibernate.initialize(encomenda.getProdutos());
            Hibernate.initialize(encomenda.getEmbalagensTransporte());
        }

        return encomendas;
    }

    public void patchEstado(long id, String estado) throws MyEntityNotFoundException, MessagingException {
        Encomenda encomenda = find(id);
        if (encomenda == null) {
            throw new MyEntityNotFoundException("Encomenda com id " + id + " não existe");
        }

        estado = estado.toLowerCase();
        // verificar se estado corresponde a um dos estados possíveis
        if (!estadoValido(estado)) {
            throw new IllegalArgumentException("Estado inválido (Estado tem de ser pendente, processamento, transporte ou entregue)");
        }

        if (estado.equals("extraviada")) {
            // Enviar email ao cliente a informar que a encomenda foi extraviada
            emailBean.send(encomenda.getConsumidorFinal().getEmail(), "Encomenda extraviada", "A sua encomenda foi extraviada");

        }

        encomenda.setEstado(estado);
        entityManager.merge(encomenda);
    }


    public boolean estadoValido(String estado) {
        if (estado == null) {
            return false;
        }

        // verificar se estado corresponde a um dos estados possíveis
        /* roubada, danificada ou perdida */
        return estado.equals("pendente") ||
                estado.equals("processamento") ||
                estado.equals("transporte") ||
                estado.equals("entregue") ||
                estado.equals("cancelada") ||
                estado.equals("devolvida") ||
                estado.equals("extraviada");
    }
}

