package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.FabricanteDeProdutos;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Produto;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

@Stateless
public class ProdutoBean {
    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(String nomeProduto, long fabricanteId) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(p.nomeProduto) FROM Produto p WHERE p.nomeProduto = :nomeProduto AND p.fabricante.id = :fabricanteId",
                Long.class
        );
        query.setParameter("nomeProduto", nomeProduto);
        query.setParameter("fabricanteId", fabricanteId);
        return (Long)query.getSingleResult() > 0L;
    }


    public void createProduto(String nomeProduto, long fabricanteId) throws MyEntityExistsException, MyEntityNotFoundException {

        if(exists(nomeProduto, fabricanteId)) {
            throw new MyEntityExistsException("Produto com nome " + nomeProduto + " já existe");
        }

        FabricanteDeProdutos fabricante = entityManager.find(FabricanteDeProdutos.class, fabricanteId);
        if (fabricante == null) {
            throw new MyEntityNotFoundException("Fabricante com id " + fabricanteId + " não existe");
        }

        Produto produto = new Produto(nomeProduto, fabricante);
        entityManager.persist(produto);
    }

    public Produto findProdutoById(Long id) {
        return entityManager.find(Produto.class, id);
    }

    public void updateProduto(long id, String nomeProduto, long fabricanteId) throws MyEntityNotFoundException {
        Produto produto = findProdutoById(id);
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

    public void removeProduto(long id) throws MyEntityNotFoundException {
        Produto produto = findProdutoById(id);
        if (produto == null) {
            throw new MyEntityNotFoundException("Produto com id " + id + " não existe");
        }
        entityManager.remove(produto);
    }

}
