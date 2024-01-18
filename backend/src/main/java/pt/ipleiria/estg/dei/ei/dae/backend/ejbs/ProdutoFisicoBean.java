package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.*;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class ProdutoFisicoBean {

    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private FabricanteDeProdutosBean fabricanteDeProdutosBean;
    @EJB
    private SensorBean sensorBean;

    @EJB
    private EmbalagemDeProdutoBean embalagemDeProdutoBean;

    @EJB
    private EncomendaBean encomendaBean;

    @EJB
    private ProdutoCatalogoBean produtoCatalogoBean;

    public boolean exists(String nomeProduto, String fabrincanteUsername, ProdutoCatalogo produtoCatalogo) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(p.nomeProduto) FROM ProdutoFisico p WHERE p.nomeProduto = :nomeProduto AND p.fabricante.username = :fabrincanteUsername AND p.produtoCatalogo = :produtoCatalogo",
                Long.class);
        query.setParameter("nomeProduto", nomeProduto);
        query.setParameter("fabrincanteUsername", fabrincanteUsername);
        query.setParameter("produtoCatalogo", produtoCatalogo);
        return (Long) query.getSingleResult() > 0L;
    }

    public ProdutoFisico create(long produtoCatalogoId, long encomendaId)
            throws Exception {

        ProdutoCatalogo produtoCatalogo = entityManager.find(ProdutoCatalogo.class, produtoCatalogoId);

        if (produtoCatalogo == null) {
            throw new MyEntityNotFoundException("Produto catálogo com id " + produtoCatalogo.getId() + " não existe");
        }

        FabricanteDeProdutos fabricante = fabricanteDeProdutosBean.find(produtoCatalogo.getFabricante().getUsername());
        if (fabricante == null) {
            throw new MyEntityNotFoundException(
                    "Fabricante com id " + produtoCatalogo.getFabricante().getUsername() + " não existe");
        }

        Encomenda encomenda = entityManager.find(Encomenda.class, encomendaId);
        if (encomenda == null) {
            throw new MyEntityNotFoundException("Encomenda com id " + encomenda.getId() + " não existe");
        }

        ProdutoFisico produtoFisico = null;

        try {
            produtoFisico = new ProdutoFisico(produtoCatalogo.getNomeProduto(), fabricante, produtoCatalogo, encomenda);
            entityManager.persist(produtoFisico);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }

        encomenda.addProduto(produtoFisico);
        produtoCatalogo.addProduto(produtoFisico);

        if (!produtoCatalogo.getEmbalagensACriar().isEmpty()) {
            for (TipoEmbalagemProduto embalagemACriar : produtoCatalogo.getEmbalagensACriar()) {
                EmbalagemDeProduto embalagem = new EmbalagemDeProduto(embalagemACriar.getMaterial(),
                        embalagemACriar.getAltura(), embalagemACriar.getLargura(), embalagemACriar.getComprimento(),
                        embalagemACriar.getTipoEmbalagem());
                entityManager.persist(embalagem);
                embalagemDeProdutoBean.addEmbalagemToProduto(embalagem.getId(), produtoFisico.getId());

                for (TipoSensor tipoSensor : embalagemACriar.getTipoSensor()) {
                    Sensor sensor = sensorBean.create(tipoSensor.getTipo(), tipoSensor.getUnidade());
                    sensor.setEstado(2);
                    entityManager.persist(sensor);
                    embalagem.addSensor(sensor);
                }
            }
        }

        return produtoFisico;
    }

    public ProdutoFisico find(long id) {
        return entityManager.find(ProdutoFisico.class, id);
    }

    public void update(long id, String nomeProduto, String fabricanteUsername, long produtoCatalogoId)
            throws MyEntityNotFoundException {
        ProdutoFisico produtoFisico = find(id);
        if (produtoFisico == null) {
            throw new MyEntityNotFoundException("Produto físico com id " + id + " não existe");
        }

        FabricanteDeProdutos fabricante = entityManager.find(FabricanteDeProdutos.class, fabricanteUsername);
        if (fabricante == null) {
            throw new MyEntityNotFoundException("Fabricante com id " + fabricanteUsername + " não existe");
        }

        ProdutoCatalogo produtoCatalogo = entityManager.find(ProdutoCatalogo.class, produtoCatalogoId);

        if (produtoCatalogo == null) {
            throw new MyEntityNotFoundException("Produto catálogo com id " + produtoCatalogo.getId() + " não existe");
        }

        produtoFisico.setProdutoCatalogo(produtoCatalogo);
        produtoFisico.setNomeProduto(nomeProduto);
        produtoFisico.setFabricante(fabricante);

        produtoCatalogo.addProduto(produtoFisico);
        entityManager.merge(produtoFisico);
    }

    public void remove(long id) throws MyEntityNotFoundException {
        ProdutoFisico produtoFisico = find(id);
        if (produtoFisico == null) {
            throw new MyEntityNotFoundException("Produto físico com id " + id + " não existe");
        }
        entityManager.remove(produtoFisico);
    }

    public List<ProdutoFisico> getAllProductsFisico() {
        return entityManager.createNamedQuery("getAllProductsFisico", ProdutoFisico.class).getResultList();
    }

    public ProdutoFisico getProdutoWithEmbalagens(long id) {
        ProdutoFisico produtoFisico = find(id);
        if (produtoFisico != null) {
            Hibernate.initialize(produtoFisico.getEmbalagensDeProduto());
        }
        return produtoFisico;
    }
}
