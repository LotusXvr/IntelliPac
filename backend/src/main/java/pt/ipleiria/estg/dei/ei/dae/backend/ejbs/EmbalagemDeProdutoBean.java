package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.*;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class EmbalagemDeProdutoBean {
    @PersistenceContext
    private EntityManager entityManager;

    public EmbalagemDeProduto create(String material, long altura, long largura, long comprimento, long tipoEmbalagem) {
        EmbalagemDeProduto embalagemDeProduto = new EmbalagemDeProduto(material, altura, largura, comprimento, tipoEmbalagem);
        entityManager.persist(embalagemDeProduto);
        return embalagemDeProduto;
    }

    public EmbalagemDeProduto find(Long id) {
        return entityManager.find(EmbalagemDeProduto.class, id);
    }

    public void update(long id, String material, long tipoEmbalagem) throws MyEntityNotFoundException {
        EmbalagemDeProduto embalagemDeProduto = find(id);
        if (embalagemDeProduto == null) {
            throw new MyEntityNotFoundException("Embalagem de Produto com id " + id + " não existe");
        }

        embalagemDeProduto.setMaterial(material);
        embalagemDeProduto.setTipoEmbalagem(tipoEmbalagem);
        entityManager.merge(embalagemDeProduto);
    }

    public void remove(long id) throws MyEntityNotFoundException {
        EmbalagemDeProduto embalagemDeProduto = find(id);
        if (embalagemDeProduto == null) {
            throw new MyEntityNotFoundException("Embalagem de produto com id " + id + " não existe");
        }
        entityManager.remove(embalagemDeProduto);
    }

    public List<EmbalagemDeProduto> getAllEmbalagensDeProduto() {
        return entityManager.createNamedQuery("getAllEmbalagensDeProduto", EmbalagemDeProduto.class).getResultList();
    }


    public void associateSensorToEmbalagem(long idEmbalagem, long idSensor) throws MyEntityNotFoundException {
        EmbalagemDeProduto embalagemDeProduto = find(idEmbalagem);
        if (embalagemDeProduto == null) {
            throw new MyEntityNotFoundException("Embalagem with id " + idEmbalagem + " not found");
        }
        Sensor sensor = entityManager.find(Sensor.class, idSensor);
        if (sensor == null) {
            throw new MyEntityNotFoundException("Sensor with id " + idSensor + " not found");
        }
        embalagemDeProduto.addSensor(sensor);
        sensor.setEstado(2);
        entityManager.merge(embalagemDeProduto);
    }

    public void removeSensorFromEmbalagem(long idEmbalagem, long idSensor) throws MyEntityNotFoundException {
        EmbalagemDeProduto embalagemDeProduto = find(idEmbalagem);
        if (embalagemDeProduto == null) {
            throw new MyEntityNotFoundException("Embalagem with id " + idEmbalagem + " not found");
        }
        Sensor sensor = entityManager.find(Sensor.class, idSensor);
        if (sensor == null) {
            throw new MyEntityNotFoundException("Sensor with id " + idSensor + " not found");
        }
        embalagemDeProduto.removeSensor(sensor);
        entityManager.merge(embalagemDeProduto);
    }

    public EmbalagemDeProduto getEmbalagemDeProdutoWithDetails(long id) {
        EmbalagemDeProduto embalagemDeProduto = find(id);
        if (embalagemDeProduto != null) {
            Hibernate.initialize(embalagemDeProduto.getSensores());
            Hibernate.initialize(embalagemDeProduto.getProdutos());
        }
        return embalagemDeProduto;
    }

    public void addEmbalagemToProduto(long idEmbalagem, long idProduto) throws Exception {
        EmbalagemDeProduto embalagemDeProduto = find(idEmbalagem);
        if (embalagemDeProduto == null) {
            throw new MyEntityNotFoundException("Embalagem with id " + idEmbalagem + " not found");
        }
        ProdutoFisico produtoFisico = entityManager.find(ProdutoFisico.class, idProduto);
        if (produtoFisico == null) {
            throw new MyEntityNotFoundException("Produto with id " + idProduto + " not found");
        }
        if (embalagemDeProduto.getTipoEmbalagem() != 1 && produtoFisico.getEmbalagensDeProduto().size() == 0) {
            throw new Exception("O produto não tem embalagem primária");
        }

        if (embalagemDeProduto.getTipoEmbalagem() == 1 && produtoFisico.getEmbalagensDeProduto().size() == 0) {
            produtoFisico.addEmbalagem(embalagemDeProduto);
            entityManager.merge(embalagemDeProduto);
            return;
        }

        EmbalagemDeProduto anterior = produtoFisico.getEmbalagensDeProduto().get(produtoFisico.getEmbalagensDeProduto().size() - 1);
        if (anterior.getTipoEmbalagem() != (embalagemDeProduto.getTipoEmbalagem() - 1)) {
            throw new Exception("O produto tem de seguir o padrão: Primária > Secundária > terciária");
        }

        if (anterior.getAltura() >= embalagemDeProduto.getAltura() || anterior.getLargura() >= embalagemDeProduto.getLargura() || anterior.getComprimento() >= embalagemDeProduto.getComprimento()) {
            throw new Exception("A embalagem a adicionar têm de ser maior que: " + anterior.getComprimento() + "X" + anterior.getLargura() + "X" + anterior.getAltura());
        }

        produtoFisico.addEmbalagem(embalagemDeProduto);
        entityManager.merge(embalagemDeProduto);
    }

    public void addTipoEmbalagemToProdutoCatalogo(long idEmbalagem, long idProduto) throws Exception {
        TipoEmbalagemProduto embalagemACriar = entityManager.find(TipoEmbalagemProduto.class, idEmbalagem);
        if (embalagemACriar == null) {
            throw new MyEntityNotFoundException("Embalagem with id " + idEmbalagem + " not found");
        }
        ProdutoCatalogo produtoCatalogo = entityManager.find(ProdutoCatalogo.class, idProduto);
        if (produtoCatalogo == null) {
            throw new MyEntityNotFoundException("Produto with id " + idProduto + " not found");
        }
        if (embalagemACriar.getTipoEmbalagem() != 1 && produtoCatalogo.getEmbalagensACriar().size() == 0) {
            throw new Exception("O produto não tem embalagem primária");
        }

        if (embalagemACriar.getTipoEmbalagem() == 1 && produtoCatalogo.getEmbalagensACriar().size() == 0) {
            produtoCatalogo.addEmbalagemACriar(embalagemACriar);
            entityManager.merge(embalagemACriar);
            return;
        }

        TipoEmbalagemProduto anterior = produtoCatalogo.getEmbalagensACriar().get(produtoCatalogo.getEmbalagensACriar().size() - 1);
        if (anterior.getTipoEmbalagem() != (embalagemACriar.getTipoEmbalagem() - 1)) {
            throw new Exception("O produto tem de seguir o padrão: Primária > Secundária > terciária");
        }

        if (anterior.getAltura() >= embalagemACriar.getAltura() || anterior.getLargura() >= embalagemACriar.getLargura() || anterior.getComprimento() >= embalagemACriar.getComprimento()) {
            throw new Exception("A embalagem a adicionar têm de ser maior que: " + anterior.getComprimento() + "X" + anterior.getLargura() + "X" + anterior.getAltura());
        }

        produtoCatalogo.addEmbalagemACriar(embalagemACriar);
        entityManager.merge(embalagemACriar);
    }

    private String tipoEmbalagemToString(long tipo) {
        switch ((int) tipo) {
            case 1:
                return "Primária";
            case 2:
                return "Secundária";
            case 3:
                return "Terciária";
            default:
                return "Desconhecido";
        }
    }

    public void removeProdutoToEmbalagem(long idEmbalagem, long idProduto) throws MyEntityNotFoundException {
        EmbalagemDeProduto embalagemDeProduto = find(idEmbalagem);
        if (embalagemDeProduto == null) {
            throw new MyEntityNotFoundException("Embalagem with id " + idEmbalagem + " not found");
        }
        ProdutoFisico produtoFisico = entityManager.find(ProdutoFisico.class, idProduto);
        if (produtoFisico == null) {
            throw new MyEntityNotFoundException("Produto with id " + idProduto + " not found");
        }
        produtoFisico.removeEmbalagem(embalagemDeProduto);
        entityManager.merge(embalagemDeProduto);
    }
}
