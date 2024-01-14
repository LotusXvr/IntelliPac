package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.FabricanteDeProdutos;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.ProdutoCatalogo;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.TipoEmbalagemProduto;
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


    public ProdutoCatalogo create(String nomeProduto, String fabrincanteUsername, long peso) throws Exception {

        if(peso <= 0){
            throw new Exception("Peso "+peso+" Kg não é valido");
        }
        if(exists(nomeProduto, fabrincanteUsername)) {
            throw new MyEntityExistsException("Produto catálogo com nome " + nomeProduto + " já existe");
        }

        FabricanteDeProdutos fabricante = fabricanteDeProdutosBean.find(fabrincanteUsername);
        if (fabricante == null) {
            throw new MyEntityNotFoundException("Fabricante com id " + fabrincanteUsername + " não existe");
        }

        ProdutoCatalogo produtoCatalogo = null;

        try {
            produtoCatalogo = new ProdutoCatalogo(nomeProduto, fabricante, peso);
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

    public ProdutoCatalogo getProdutoCatalogoWithProdutos(long id) {
        ProdutoCatalogo produtoCatalogo = find(id);
        if (produtoCatalogo != null) {
            Hibernate.initialize(produtoCatalogo.getProdutos());
            Hibernate.initialize(produtoCatalogo.getEmbalagensACriar());
        }
        return produtoCatalogo;
    }

    public void addTipoEmbalagem(long idTipoEmbalagem, long idProduto) {
        // Find the student by username
        ProdutoCatalogo produtoCatalogo = find(idProduto);
        if (produtoCatalogo == null) {
            throw new IllegalArgumentException("Produto with id " + idProduto + " not found.");
        }

        // Find the subject by subject code
        TipoEmbalagemProduto tipoEmbalagemProduto = entityManager.find(TipoEmbalagemProduto.class, idTipoEmbalagem);
        if (tipoEmbalagemProduto == null) {
            throw new IllegalArgumentException("Tipo Embalagem with id " + idTipoEmbalagem + " not found.");
        }

        // Enroll the student in the subject
        produtoCatalogo.addEmbalagemACriar(tipoEmbalagemProduto);
    }

    public void removeTipoEmbalagem(long idTipoEmbalagem, long idProduto) {
        // Find the student by username
        ProdutoCatalogo produtoCatalogo = find(idProduto);
        if (produtoCatalogo == null) {
            throw new IllegalArgumentException("Produto with id " + idProduto + " not found.");
        }

        // Find the subject by subject code
        TipoEmbalagemProduto tipoEmbalagemProduto = entityManager.find(TipoEmbalagemProduto.class, idTipoEmbalagem);
        if (tipoEmbalagemProduto == null) {
            throw new IllegalArgumentException("Tipo Embalagem with id " + idTipoEmbalagem + " not found.");
        }

        // Enroll the student in the subject
        produtoCatalogo.removeEmbalagemACriar(tipoEmbalagemProduto);
    }
}
