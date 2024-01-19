package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
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


    public boolean exists(long tipo, String material, long altura, long largura, long comprimento) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(p.tipoEmbalagem) FROM TipoEmbalagemProduto p WHERE p.tipoEmbalagem = :tipo AND p.material = :material AND p.altura = :altura AND p.largura = :largura AND p.comprimento = :comprimento",
                Long.class
        );
        query.setParameter("tipo", tipo);
        query.setParameter("material", material);
        query.setParameter("altura", altura);
        query.setParameter("largura", largura);
        query.setParameter("comprimento", comprimento);
        return (Long)query.getSingleResult() > 0L;
    }

    public TipoEmbalagemProduto create(long tipo, String material, long altura, long largura, long comprimento) throws Exception {

        if(exists(tipo, material, altura, largura, comprimento)) {
            throw new MyEntityExistsException("Tipo Embalagem com tipo " + tipo + " e material"+material+" com as mesmas dimensões já existe");
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

    public void update(long id, long tipo, String material,long altura, long largura, long comprimento) throws MyEntityNotFoundException, MyEntityExistsException {
        TipoEmbalagemProduto tipoEmbalagemProduto = find(id);
        if (tipoEmbalagemProduto == null) {
            throw new MyEntityNotFoundException("Tipo Embalagem com id " + id + " não existe");
        }

        if(exists(tipo, material, altura, largura, comprimento)) {
            throw new MyEntityExistsException("Tipo Embalagem com tipo " + tipo + " e material"+material+" com as mesmas dimensões já existe");
        }

        tipoEmbalagemProduto.setTipoEmbalagem(tipo);
        tipoEmbalagemProduto.setMaterial(material);
        tipoEmbalagemProduto.setAltura(altura);
        tipoEmbalagemProduto.setLargura(largura);
        tipoEmbalagemProduto.setComprimento(comprimento);
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

    public TipoEmbalagemProduto getEmbalagemWithSensores(long id) throws MyEntityNotFoundException {
        TipoEmbalagemProduto tipoEmbalagemProduto = find(id);
        if(tipoEmbalagemProduto == null){
            throw new MyEntityNotFoundException("Nao existe um tipo de Embalagem com o id" + id);
        }
        Hibernate.initialize(tipoEmbalagemProduto.getTipoSensor());
        return tipoEmbalagemProduto;

    }
}
