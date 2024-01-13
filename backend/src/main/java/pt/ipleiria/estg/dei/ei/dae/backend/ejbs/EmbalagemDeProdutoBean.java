package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.EmbalagemDeProduto;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.FabricanteDeProdutos;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.ProdutoCatalogo;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class EmbalagemDeProdutoBean {
    @PersistenceContext
    private EntityManager entityManager;

    public EmbalagemDeProduto create(String material, String tipoEmbalagem) {
        EmbalagemDeProduto embalagemDeProduto = new EmbalagemDeProduto(material,tipoEmbalagem);
        entityManager.persist(embalagemDeProduto);
        return embalagemDeProduto;
    }

    public EmbalagemDeProduto find(Long id) {
        return entityManager.find(EmbalagemDeProduto.class, id);
    }

    public void update(long id, String material, String tipoEmbalagem) throws MyEntityNotFoundException {
        EmbalagemDeProduto embalagemDeProduto = find(id);
        if (embalagemDeProduto == null) {
            throw new MyEntityNotFoundException("Produto catálogo com id " + id + " não existe");
        }

        embalagemDeProduto.setMaterial(material);
        embalagemDeProduto.setTipoEmbalagem(tipoEmbalagem);
        entityManager.merge(embalagemDeProduto);
    }

    public void remove(long id) throws MyEntityNotFoundException {
        EmbalagemDeProduto embalagemDeProduto = find(id);
        if (embalagemDeProduto == null) {
            throw new MyEntityNotFoundException("Produto catálogo com id " + id + " não existe");
        }
        entityManager.remove(embalagemDeProduto);
    }

    public List<EmbalagemDeProduto> getAllEmbalagensDeProduto() {
        return entityManager.createNamedQuery("getAllEmbalagensDeProduto", EmbalagemDeProduto.class).getResultList();
    }
}
