package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.FabricanteDeProdutos;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.ProdutoCatalogo;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.ProdutoFisico;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

@Stateless
public class ProdutoFisicoBean {

    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private FabricanteDeProdutosBean fabricanteDeProdutosBean;

    public boolean exists(String nomeProduto, String fabrincanteUsername, ProdutoCatalogo produtoCatalogo) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(p.nomeProduto) FROM ProdutoFisico p WHERE p.nomeProduto = :nomeProduto AND p.fabricante.username = :fabrincanteUsername AND p.produtoCatalogo = :produtoCatalogo",
                Long.class
        );
        query.setParameter("nomeProduto", nomeProduto);
        query.setParameter("fabrincanteUsername", fabrincanteUsername);
        query.setParameter("produtoCatalogo", produtoCatalogo);
        return (Long)query.getSingleResult() > 0L;
    }

    public ProdutoFisico create(String nomeProduto, String fabrincanteUsername, ProdutoCatalogo produtoCatalogo) throws MyEntityExistsException, MyEntityNotFoundException {

        if(exists(nomeProduto, fabrincanteUsername, produtoCatalogo)) {
            throw new MyEntityExistsException("Produto físico com nome " + nomeProduto + " já existe");
        }

        var fabricante = fabricanteDeProdutosBean.find(fabrincanteUsername);
        if (fabricante == null) {
            throw new MyEntityNotFoundException("Fabricante com id " + fabrincanteUsername + " não existe");
        }

        ProdutoFisico produtoFisico = null;

        try {
            produtoFisico = new ProdutoFisico(nomeProduto, fabricante, produtoCatalogo);
            entityManager.persist(produtoFisico);
        }
        catch (Exception e) {
            throw new MyEntityExistsException("Produto físico com nome " + nomeProduto + " já existe");
        }

        return produtoFisico;
    }

    public ProdutoFisico find(long id) {
        return entityManager.find(ProdutoFisico.class, id);
    }

    public void update(long id, String nomeProduto, String fabrincanteUsername, ProdutoCatalogo produtoCatalogo) throws MyEntityNotFoundException {
        ProdutoFisico produtoFisico = find(id);
        if (produtoFisico == null) {
            throw new MyEntityNotFoundException("Produto físico com id " + id + " não existe");
        }

        FabricanteDeProdutos fabricante = entityManager.find(FabricanteDeProdutos.class, fabrincanteUsername);
        if (fabricante == null) {
            throw new MyEntityNotFoundException("Fabricante com id " + fabrincanteUsername + " não existe");
        }

        produtoFisico.setNomeProduto(nomeProduto);
        produtoFisico.setFabricante(fabricante);
        entityManager.merge(produtoFisico);
    }

    public void remove(long id) throws MyEntityNotFoundException {
        ProdutoFisico produtoFisico = find(id);
        if (produtoFisico == null) {
            throw new MyEntityNotFoundException("Produto físico com id " + id + " não existe");
        }
        entityManager.remove(produtoFisico);
    }
}
