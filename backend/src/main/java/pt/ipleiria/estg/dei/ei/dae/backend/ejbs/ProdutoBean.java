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
public class ProdutoBean {
    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private FabricanteDeProdutosBean fabricanteDeProdutosBean;

    public boolean exists(String nomeProduto, long fabricanteId) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(p.nomeProduto) FROM Produto p WHERE p.nomeProduto = :nomeProduto AND p.fabricante.id = :fabricanteId",
                Long.class
        );
        query.setParameter("nomeProduto", nomeProduto);
        query.setParameter("fabricanteId", fabricanteId);
        return (Long)query.getSingleResult() > 0L;
    }


    public Produto createProduto(String nomeProduto, long fabricanteId) throws MyEntityExistsException, MyEntityNotFoundException {

        if(exists(nomeProduto, fabricanteId)) {
            throw new MyEntityExistsException("Produto com nome " + nomeProduto + " já existe");
        }

        var fabricante = fabricanteDeProdutosBean.find(fabricanteId);
        if (fabricante == null) {
            throw new MyEntityNotFoundException("Fabricante com id " + fabricanteId + " não existe");
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

    public Produto findProduto(long id) {
        return entityManager.find(Produto.class, id);
    }

    public void update(long id, String nomeProduto, long fabricanteId) throws MyEntityNotFoundException {
        Produto produto = findProduto(id);
        if (produto == null) {
            throw new MyEntityNotFoundException("Produto com id " + id + " não existe");
        }

        FabricanteDeProdutos fabricante = entityManager.find(FabricanteDeProdutos.class, fabricanteId);
        if (fabricante == null) {
            throw new MyEntityNotFoundException("Fabricante com id " + fabricanteId + " não existe");
        }

        produto.setNomeProduto(nomeProduto);
        produto.setFabricante(fabricante);
        entityManager.merge(produto);
    }

    public void remove(long id) throws MyEntityNotFoundException {
        Produto produto = findProduto(id);
        if (produto == null) {
            throw new MyEntityNotFoundException("Produto com id " + id + " não existe");
        }
        entityManager.remove(produto);
    }

    public List<Produto> getAllProducts() {
        return entityManager.createNamedQuery("getAllProducts", Produto.class).getResultList();
    }
}
