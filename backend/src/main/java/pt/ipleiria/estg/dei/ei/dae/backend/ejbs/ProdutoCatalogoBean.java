package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoCatalogoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.TipoEmbalagemDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.FabricanteDeProdutos;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.ProdutoCatalogo;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.TipoEmbalagemProduto;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.Comparator;
import java.util.List;

@Stateless
public class ProdutoCatalogoBean {
    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private FabricanteDeProdutosBean fabricanteDeProdutosBean;

    @EJB
    private EmbalagemDeProdutoBean embalagemProdutoBean;

    public boolean exists(String nomeProduto, String fabrincanteUsername) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(p.nomeProduto) FROM ProdutoCatalogo p WHERE p.nomeProduto = :nomeProduto AND p.fabricante.username = :fabrincanteUsername",
                Long.class
        );
        query.setParameter("nomeProduto", nomeProduto);
        query.setParameter("fabrincanteUsername", fabrincanteUsername);
        return (Long) query.getSingleResult() > 0L;
    }


    public ProdutoCatalogo create(ProdutoCatalogoDTO produtoCatalogoDTO) throws Exception {
        try {
            FabricanteDeProdutos fabricante = fabricanteDeProdutosBean.find(produtoCatalogoDTO.getFabricanteUsername());
            if (fabricante == null) {
                throw new MyEntityNotFoundException("Fabricante com id " + produtoCatalogoDTO.getFabricanteUsername() + " não existe");
            }

            ProdutoCatalogo produtoCatalogo = new ProdutoCatalogo(
                    produtoCatalogoDTO.getNome(),
                    fabricante,
                    produtoCatalogoDTO.getPeso()
            );

            for (TipoEmbalagemDTO tipoEmbalagemDTO : produtoCatalogoDTO.getEmbalagensACriar()) {
                TipoEmbalagemProduto tipoEmbalagemProduto = entityManager.find(TipoEmbalagemProduto.class, tipoEmbalagemDTO.getId());
                if (tipoEmbalagemProduto == null) {
                    throw new MyEntityNotFoundException("Tipo Embalagem with id " + tipoEmbalagemDTO.getId() + " not found.");
                }
                entityManager.persist(produtoCatalogo);
                try {

                    embalagemProdutoBean.addTipoEmbalagemToProdutoCatalogo(tipoEmbalagemProduto.getId(), produtoCatalogo.getId());
                } catch (Exception e) {
                    entityManager.remove(produtoCatalogo);
                    throw new MyConstraintViolationException((ConstraintViolationException) e);
                }
            }

            entityManager.persist(produtoCatalogo);
            return produtoCatalogo;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public ProdutoCatalogo find(long id) {
        return entityManager.find(ProdutoCatalogo.class, id);
    }

    public ProdutoCatalogo update(long id, ProdutoCatalogoDTO produtoCatalogoDTO) throws MyEntityNotFoundException, MyConstraintViolationException {
        try {
            ProdutoCatalogo produtoCatalogo = find(id);
            if (produtoCatalogo == null) {
                throw new MyEntityNotFoundException("Produto catálogo com id " + produtoCatalogoDTO.getId() + " não existe");
            }

            FabricanteDeProdutos fabricante = fabricanteDeProdutosBean.find(produtoCatalogoDTO.getFabricanteUsername());
            if (fabricante == null) {
                throw new MyEntityNotFoundException("Fabricante com id " + produtoCatalogoDTO.getFabricanteUsername() + " não existe");
            }

            entityManager.lock(produtoCatalogo, LockModeType.OPTIMISTIC);

            produtoCatalogo.setNomeProduto(produtoCatalogoDTO.getNome());
            produtoCatalogo.setFabricante(fabricante);
            produtoCatalogo.setPeso(produtoCatalogoDTO.getPeso());
            produtoCatalogo.getEmbalagensACriar().clear();

            for (TipoEmbalagemDTO tipoEmbalagemDTO : produtoCatalogoDTO.getEmbalagensACriar()) {
                TipoEmbalagemProduto tipoEmbalagemProduto = entityManager.find(TipoEmbalagemProduto.class, tipoEmbalagemDTO.getId());

                if (tipoEmbalagemProduto == null) {
                    throw new MyEntityNotFoundException("Tipo Embalagem with id " + tipoEmbalagemDTO.getId() + " not found.");
                }

                entityManager.persist(tipoEmbalagemProduto);
                embalagemProdutoBean.addTipoEmbalagemToProdutoCatalogo(tipoEmbalagemProduto.getId(), produtoCatalogo.getId());

            }

            entityManager.merge(produtoCatalogo);
            return produtoCatalogo;
        } catch (Exception e) {
            throw new MyConstraintViolationException((ConstraintViolationException) e);
        }
    }

    public void remove(long id) throws MyEntityNotFoundException {
        ProdutoCatalogo produtoCatalogo = find(id);
        if (produtoCatalogo == null) {
            throw new MyEntityNotFoundException("Produto catálogo com id " + id + " não existe");
        }
        if (produtoCatalogo.getProdutos().size() != 0){
            throw new MyEntityNotFoundException("Produto catalogo não pode ser eliminado pois existem produtos fisicos associados");
        }
        entityManager.remove(produtoCatalogo);
    }

    public List<ProdutoCatalogo> getAllProductsCatalogo() {
        List<ProdutoCatalogo> produtosCatalogo = entityManager.createNamedQuery("getAllProductsCatalogo", ProdutoCatalogo.class).getResultList();
        for (ProdutoCatalogo produtoCatalogo : produtosCatalogo) {
            produtoCatalogo.getEmbalagensACriar().sort(Comparator.comparingLong(TipoEmbalagemProduto::getTipoEmbalagem));
            Hibernate.initialize(produtoCatalogo.getProdutos());
            Hibernate.initialize(produtoCatalogo.getEmbalagensACriar());
        }
        return produtosCatalogo;
    }

    public ProdutoCatalogo getProdutoCatalogoWithProdutos(long id) {
        ProdutoCatalogo produtoCatalogo = find(id);
        List<TipoEmbalagemProduto> embalagensDeProduto = produtoCatalogo.getEmbalagensACriar();
        produtoCatalogo.getEmbalagensACriar().sort(Comparator.comparingLong(TipoEmbalagemProduto::getTipoEmbalagem));
        for (TipoEmbalagemProduto embalagemDeProduto : embalagensDeProduto) {
            System.out.println(embalagemDeProduto.getId() + " 123123 " + embalagemDeProduto.getMaterial());
        }
        if (produtoCatalogo != null) {
            Hibernate.initialize(produtoCatalogo.getProdutos());
            Hibernate.initialize(embalagensDeProduto);
        }
        return produtoCatalogo;
    }

    public List<ProdutoCatalogo> getProdutosCatalogoFromFabricante(String username){
        FabricanteDeProdutos fabricanteDeProdutos = fabricanteDeProdutosBean.find(username);

        if (fabricanteDeProdutos == null) {
            throw new IllegalArgumentException("Fabricante com username " + username + " não existe");
        }

        List<ProdutoCatalogo> produtoCatalogos = entityManager.createNamedQuery("getAllProductsFromFabricante", ProdutoCatalogo.class).setParameter("username", username).getResultList();
        for (ProdutoCatalogo produtoCatalogo : produtoCatalogos) {
            produtoCatalogo.getEmbalagensACriar().sort(Comparator.comparingLong(TipoEmbalagemProduto::getTipoEmbalagem));
            Hibernate.initialize(produtoCatalogo.getProdutos());
            Hibernate.initialize(produtoCatalogo.getEmbalagensACriar());
        }
        return produtoCatalogos;
    }

    public void addTipoEmbalagem(long idEmbalagem, long idProduto) {

        ProdutoCatalogo produtoCatalogo = find(idProduto);
        if (produtoCatalogo == null) {
            throw new IllegalArgumentException("Produto with id " + idProduto + " not found.");
        }

        TipoEmbalagemProduto tipoEmbalagemProduto = entityManager.find(TipoEmbalagemProduto.class, idEmbalagem);
        if (tipoEmbalagemProduto == null) {
            throw new IllegalArgumentException("Tipo Embalagem with id " + idEmbalagem + " not found.");
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
