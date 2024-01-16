package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EmbalagemDeTransporteDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EncomendaDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoFisicoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.*;
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
    private ProdutoFisicoBean produtoFisicoBean;

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
        EmbalagemDeTransporte embalagemTransporte = null;

        try {
            encomenda = new Encomenda(cliente, getTimestamp(), operadorDeLogistica, "PENDENTE");
            entityManager.persist(encomenda);
            ProdutoFisico produtoFisico = null;
            // Adicione lógica para associar os produtosCatalogo à encomenda
            if (encomendaDTO.getProdutos() == null) {
                throw new MyConstraintViolationException(new ConstraintViolationException("A encomenda necessita ter pelo menos um produto", null));
            }
            for (ProdutoFisicoDTO produtoDTO : encomendaDTO.getProdutos()) {
                ProdutoCatalogo produtoCatalogo = entityManager.find(ProdutoCatalogo.class, produtoDTO.getProdutoCatalogoId());
                if (produtoCatalogo == null) {
                    throw new MyEntityNotFoundException("ProdutoCatalogo com id " + produtoDTO.getProdutoCatalogoId() + " não existe");
                }

                FabricanteDeProdutos fabricanteDeProdutos = entityManager.find(FabricanteDeProdutos.class, produtoCatalogo.getFabricante().getUsername());

                // Agora, em vez de usar o DTO diretamente, crie uma instância de ProdutoFisico e persista-a
                produtoFisico = produtoFisicoBean.create(produtoCatalogo.getNomeProduto(), fabricanteDeProdutos.getUsername(), produtoCatalogo.getId(), encomenda.getId());
                entityManager.persist(produtoFisico);

                // Adicione o produtoFisico à encomenda
                encomenda.addProduto(produtoFisico);
            }

            if (encomendaDTO.getEmbalagensTransporte() == null) {
                throw new MyConstraintViolationException(new ConstraintViolationException("A encomenda necessita ter pelo menos uma embalagem", null));
            }
            // Adicione lógica para associar as embalagensDeTransporte à encomenda
            for (EmbalagemDeTransporteDTO embalagemDTO : encomendaDTO.getEmbalagensTransporte()) {
                embalagemTransporte = entityManager.find(EmbalagemDeTransporte.class, embalagemDTO.getId());
                if (embalagemTransporte == null) {
                    throw new MyEntityNotFoundException("EmbalagemDeTransporte com id " + embalagemDTO.getId() + " não existe");
                }

                // Adicione a embalagemDeTransporte à encomenda
                encomenda.addEmbalagemTransporte(embalagemTransporte);
                embalagemTransporte.addEncomenda(encomenda);
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

        return getEncomendasWithHibernateLists(encomendas);
    }

    public List<Encomenda> getAllEncomendasOperadoresLogistica(String operadorUsername) {
        var operadorLogistica = operadorDeLogisticaBean.find(operadorUsername);

        if (operadorLogistica == null) {
            throw new IllegalArgumentException("Operador de logistica com username " + operadorUsername + " não existe");
        }

        List<Encomenda> encomendas = entityManager.createNamedQuery("getAllEncomendasOperadoresLogistica", Encomenda.class).setParameter("operador", operadorLogistica).getResultList();

        return getEncomendasWithHibernateLists(encomendas);
    }

    private List<Encomenda> getEncomendasWithHibernateLists(List<Encomenda> encomendas) {
        for (Encomenda encomenda : encomendas) {
            List<ProdutoFisico> produtos = encomenda.getProdutos();
            Hibernate.initialize(produtos);
            for (ProdutoFisico produto : produtos) {
                Hibernate.initialize(produto.getEmbalagensDeProduto());
            }
            Hibernate.initialize(encomenda.getEmbalagensTransporte());
        }

        return encomendas;
    }

    public List<Encomenda> getAllEncomendas() {
        List<Encomenda> encomendas = entityManager.createQuery("SELECT e FROM Encomenda e", Encomenda.class)
                .getResultList();

        return getEncomendasWithHibernateLists(encomendas);
    }

    public String getTimestamp() {
        LocalDateTime timestampToFormat = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return timestampToFormat.format(formatter);
    }

    public Encomenda getEncomendaById(long id) {
        Encomenda encomenda = entityManager.find(Encomenda.class, id);
        if (encomenda != null) {
            List<ProdutoFisico> produtos = encomenda.getProdutos();
            Hibernate.initialize(produtos);
            for (ProdutoFisico produto : produtos) {
                Hibernate.initialize(produto.getEmbalagensDeProduto());
            }
            Hibernate.initialize(encomenda.getEmbalagensTransporte());

        }
        return encomenda;

    }

    public List<Encomenda> getEncomendasByEstado(String estado) {
        if (estado == null) {
            throw new IllegalArgumentException("Estado não pode ser null");
        }

        estado = estado.toUpperCase();
        // verificar se estado corresponde a um dos estados possíveis
        if (!estadoValido(estado)) {
            throw new IllegalArgumentException("Estado inválido (Estado tem de ser pendente, processamento, transporte ou entregue)");
        }

        List<Encomenda> encomendas = entityManager.createNamedQuery("getEncomendasByEstado", Encomenda.class).setParameter("estado", estado.toUpperCase()).getResultList();

        for (Encomenda encomenda : encomendas) {
            Hibernate.initialize(encomenda.getProdutos());
            Hibernate.initialize(encomenda.getEmbalagensTransporte());
        }

        return encomendas;
    }

    public void patchEstado(long id, String estado) throws MyEntityNotFoundException {
        Encomenda encomenda = find(id);
        if (encomenda == null) {
            throw new MyEntityNotFoundException("Encomenda com id " + id + " não existe");
        }

        estado = estado.toUpperCase();
        // verificar se estado corresponde a um dos estados possíveis
        if (!estadoValido(estado)) {
            throw new IllegalArgumentException("Estado inválido (Estado tem de ser pendente, processamento, transporte, entrega, cancelada, devolvida, danificada ou perdida)");
        }

        if (estado.equals("DANIFICADA") || estado.equals("PERDIDA")) {
            // Enviar email ao cliente a informar que a encomenda foi extraviada
            emailBean.send(encomenda.getConsumidorFinal().getEmail(), "Encomenda " + estado.toLowerCase(), "A sua encomenda foi " + estado.toLowerCase() + ".\n" +
                    "Por favor, contacte o operador de logistica para mais informacoes.");

        }

        encomenda.setEstado(estado);
        entityManager.merge(encomenda);
    }


    public boolean estadoValido(String estado) {
        if (estado == null) {
            return false;
        }

        return estado.equals("PENDENTE") ||
                estado.equals("PROCESSAMENTO") ||
                estado.equals("TRANSPORTE") ||
                estado.equals("ENTREGUE") ||
                estado.equals("CANCELADA") ||
                estado.equals("DEVOLVIDA") ||
                estado.equals("DANIFICADA") ||
                estado.equals("PERDIDA");
    }
}

