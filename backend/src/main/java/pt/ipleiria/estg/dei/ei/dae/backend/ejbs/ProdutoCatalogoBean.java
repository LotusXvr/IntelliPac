package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.FabricanteDeProdutos;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Produto;
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
                "SELECT COUNT(p.nomeProduto) FROM Produto p WHERE p.nomeProduto = :nomeProduto AND p.fabricante.username = :fabrincanteUsername",
                Long.class
        );
        query.setParameter("nomeProduto", nomeProduto);
        query.setParameter("fabrincanteUsername", fabrincanteUsername);
        return (Long)query.getSingleResult() > 0L;
    }


    public Produto create(String nomeProduto, String fabrincanteUsername) throws MyEntityExistsException, MyEntityNotFoundException {

        if(exists(nomeProduto, fabrincanteUsername)) {
            throw new MyEntityExistsException("Produto com nome " + nomeProduto + " já existe");
        }

        var fabricante = fabricanteDeProdutosBean.find(fabrincanteUsername);
        if (fabricante == null) {
            throw new MyEntityNotFoundException("Fabricante com id " + fabrincanteUsername + " não existe");
        }

        Produto produto = null;

        try {
            produto = new Produto(nomeProduto, fabricante);
            entityManager.persist(produto);
        }
        catch (Exception e) {
            throw new MyEntityExistsException("Produto com nome " + nomeProduto + " já existe");
        }

        fabricante.addProduto(produto);
        return produto;

    }

    public Produto find(long id) {
        return entityManager.find(Produto.class, id);
    }

    public void update(long id, String nomeProduto, String fabrincanteUsername) throws MyEntityNotFoundException {
        Produto produto = find(id);
        if (produto == null) {
            throw new MyEntityNotFoundException("Produto com id " + id + " não existe");
        }

        FabricanteDeProdutos fabricante = entityManager.find(FabricanteDeProdutos.class, fabrincanteUsername);
        if (fabricante == null) {
            throw new MyEntityNotFoundException("Fabricante com id " + fabrincanteUsername + " não existe");
        }

        produto.setNomeProduto(nomeProduto);
        produto.setFabricante(fabricante);
        entityManager.merge(produto);
    }

    public void remove(long id) throws MyEntityNotFoundException {
        Produto produto = find(id);
        if (produto == null) {
            throw new MyEntityNotFoundException("Produto com id " + id + " não existe");
        }
        entityManager.remove(produto);
    }

    public List<Produto> getAllProducts() {
        return entityManager.createNamedQuery("getAllProducts", Produto.class).getResultList();
    }
}
