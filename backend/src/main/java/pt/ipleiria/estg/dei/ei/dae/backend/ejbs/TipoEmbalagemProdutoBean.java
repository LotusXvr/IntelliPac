package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.FabricanteDeProdutos;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.ProdutoCatalogo;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.TipoEmbalagemProduto;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.TipoSensor;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class TipoEmbalagemProdutoBean {
    @PersistenceContext
    private EntityManager entityManager;


    public boolean exists(long tipo, String material) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(p.tipoEmbalagem) FROM TipoEmbalagemProduto p WHERE p.tipoEmbalagem = :tipo AND p.material = :material",
                Long.class
        );
        query.setParameter("tipo", tipo);
        query.setParameter("material", material);
        return (Long)query.getSingleResult() > 0L;
    }

    public TipoEmbalagemProduto create(long tipo, String material, long altura, long largura, long comprimento) throws Exception {

        if(exists(tipo, material)) {
            throw new MyEntityExistsException("Tipo Embalagem com tipo " + tipo + " e material"+material+" já existe");
        }

        TipoEmbalagemProduto tipoEmbalagemProduto = null;

        try {
            tipoEmbalagemProduto = new TipoEmbalagemProduto(tipo, material, altura, largura, comprimento);
            entityManager.persist(tipoEmbalagemProduto);
        }
        catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
        return tipoEmbalagemProduto;
    }

    public TipoEmbalagemProduto find(long id) {
        return entityManager.find(TipoEmbalagemProduto.class, id);
    }

    public void update(long id, long tipo, String material) throws MyEntityNotFoundException, MyEntityExistsException {
        TipoEmbalagemProduto tipoEmbalagemProduto = find(id);
        if (tipoEmbalagemProduto == null) {
            throw new MyEntityNotFoundException("Tipo Embalagem com id " + id + " não existe");
        }

        if(exists(tipo, material)) {
            throw new MyEntityExistsException("Tipo Embalagem com tipo " + tipo + " e material"+material+" já existe");
        }

        tipoEmbalagemProduto.setTipoEmbalagem(tipo);
        tipoEmbalagemProduto.setMaterial(material);
        entityManager.merge(tipoEmbalagemProduto);
    }

    public void remove(long id) throws MyEntityNotFoundException {
        TipoEmbalagemProduto tipoEmbalagemProduto = find(id);
        if (tipoEmbalagemProduto == null) {
            throw new MyEntityNotFoundException("Tipo Embalagem com id " + id + " não existe");
        }
        entityManager.remove(tipoEmbalagemProduto);
    }

    public List<TipoEmbalagemProduto> getAllTipoEmbalagem() {
        return entityManager.createNamedQuery("getAllTipoEmbalagem", TipoEmbalagemProduto.class).getResultList();
    }

    public void addTipoSensor(long idTipoEmbalagemProduto, long idTipoSensor) {

        TipoEmbalagemProduto tipoEmbalagemProduto = find(idTipoEmbalagemProduto);
        if (tipoEmbalagemProduto == null) {
            throw new IllegalArgumentException("Tipo Embalagem with id " + idTipoEmbalagemProduto + " not found.");
        }

        TipoSensor tipoSensor = entityManager.find(TipoSensor.class, idTipoSensor);
        if (tipoSensor == null) {
            throw new IllegalArgumentException("Tipo Sensor with id " + idTipoSensor + " not found.");
        }
        tipoEmbalagemProduto.addTipoSensor(tipoSensor);
    }

    public void removeTipoSensor(long idTipoEmbalagemProduto, long idTipoSensor) {

        TipoEmbalagemProduto tipoEmbalagemProduto = find(idTipoEmbalagemProduto);
        if (tipoEmbalagemProduto == null) {
            throw new IllegalArgumentException("Tipo Embalagem with id " + idTipoEmbalagemProduto + " not found.");
        }

        TipoSensor tipoSensor = entityManager.find(TipoSensor.class, idTipoSensor);
        if (tipoSensor == null) {
            throw new IllegalArgumentException("Tipo Sensor with id " + idTipoSensor + " not found.");
        }
        tipoEmbalagemProduto.removeTipoSensor(tipoSensor);
    }
}
