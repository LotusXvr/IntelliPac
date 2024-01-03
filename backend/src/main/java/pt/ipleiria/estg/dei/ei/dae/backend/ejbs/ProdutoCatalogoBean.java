package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.FabricanteDeProdutos;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.ProdutoCatalogo;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class ProdutoCatalogoBean {
    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private FabricanteDeProdutosBean fabricanteDeProdutosBean;

    public boolean exists(String nomeProduto, String fabrincanteUsername) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(p.nomeProduto) FROM ProdutoCatalogo p WHERE p.nomeProduto = :nomeProduto AND p.fabricante.username = :fabrincanteUsername",
                Long.class
        );
        query.setParameter("nomeProduto", nomeProduto);
        query.setParameter("fabrincanteUsername", fabrincanteUsername);
        return (Long)query.getSingleResult() > 0L;
    }


    public ProdutoCatalogo create(String nomeProduto, String fabrincanteUsername) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {

        if(exists(nomeProduto, fabrincanteUsername)) {
            throw new MyEntityExistsException("Produto catálogo com nome " + nomeProduto + " já existe");
        }

        var fabricante = fabricanteDeProdutosBean.find(fabrincanteUsername);
        if (fabricante == null) {
            throw new MyEntityNotFoundException("Fabricante com id " + fabrincanteUsername + " não existe");
        }

        ProdutoCatalogo produtoCatalogo = null;

        try {
            produtoCatalogo = new ProdutoCatalogo(nomeProduto, fabricante);
            entityManager.persist(produtoCatalogo);
        }
        catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }

        fabricante.addProduto(produtoCatalogo);
        return produtoCatalogo;

    }

    public ProdutoCatalogo find(long id) {
        return entityManager.find(ProdutoCatalogo.class, id);
    }

    public void update(long id, String nomeProduto, String fabrincanteUsername) throws MyEntityNotFoundException {
        ProdutoCatalogo produtoCatalogo = find(id);
        if (produtoCatalogo == null) {
            throw new MyEntityNotFoundException("Produto catálogo com id " + id + " não existe");
        }

        FabricanteDeProdutos fabricante = entityManager.find(FabricanteDeProdutos.class, fabrincanteUsername);
        if (fabricante == null) {
            throw new MyEntityNotFoundException("Fabricante com id " + fabrincanteUsername + " não existe");
        }

        produtoCatalogo.setNomeProduto(nomeProduto);
        produtoCatalogo.setFabricante(fabricante);
        entityManager.merge(produtoCatalogo);
    }

    public void remove(long id) throws MyEntityNotFoundException {
        ProdutoCatalogo produtoCatalogo = find(id);
        if (produtoCatalogo == null) {
            throw new MyEntityNotFoundException("Produto catálogo com id " + id + " não existe");
        }
        entityManager.remove(produtoCatalogo);
    }

    public List<ProdutoCatalogo> getAllProductsCatalogo() {
        return entityManager.createNamedQuery("getAllProductsCatalogo", ProdutoCatalogo.class).getResultList();
    }
}
