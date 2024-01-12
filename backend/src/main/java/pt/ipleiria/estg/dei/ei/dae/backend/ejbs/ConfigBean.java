package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;


import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Cliente;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Embalagem;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.ProdutoCatalogo;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@Startup
@Singleton
public class ConfigBean {

    @EJB
    private ProdutoCatalogoBean produtoCatalogoBean;

    @EJB
    FabricanteDeProdutosBean fabricanteDeProdutosBean;

    @EJB
    OperadorDeLogisticaBean operadorDeLogisticaBean;

    @EJB
    SensorBean sensorBean;

    @EJB
    ObservacaoBean observacaoBean;

    @EJB
    ProdutoFisicoBean produtoFisicoBean;

    @EJB
    EncomendaBean encomendaBean;

    @EJB
    ClienteBean clienteBean;

    @EJB
    EmbalagemBean embalagemBean;

    private Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB() {
        System.out.println("ConfigBean: PostConstruct");

        try {
            fabricanteDeProdutosBean.create("Fabrica1", "123", "fabricante 1", "fabricante1@mail.pt");
            fabricanteDeProdutosBean.create("Fabrica2", "123", "fabricante 2", "fabricante2@mail.pt");

            operadorDeLogisticaBean.create("ValterLogo", "123", "Valte", "valtefutebole@mail.pt");
            operadorDeLogisticaBean.create("Joaoz", "123", "Joao", "joao@mail.pt");

            clienteBean.create("Emanuel", "123", "Emanuel Nunes", "emanuel@nunes.pt");

            ProdutoCatalogo produtoCatalogo1 = produtoCatalogoBean.create("produto 1", "Fabrica1");
            ProdutoCatalogo produtoCatalogo2 = produtoCatalogoBean.create("produto 2", "Fabrica2");

            Encomenda encomenda1 = encomendaBean.create("Emanuel", "ValterLogo");

            produtoFisicoBean.create("produto1Fisico", "Fabrica1", produtoCatalogo1.getId(), encomenda1.getId());
            produtoFisicoBean.create("produto2Fisico", "Fabrica1", produtoCatalogo2.getId(), encomenda1.getId());

            // Embalagens
            Embalagem embalagemPlastico = embalagemBean.create("Plastico");
            Embalagem embalagemCartao = embalagemBean.create("Cartao");
            Embalagem embalagemVidro = embalagemBean.create("Vidro");
            Embalagem embalagemMetal = embalagemBean.create("Metal");

            // Sensores
            sensorBean.create(1, "Temperatura", "ºC", embalagemPlastico.getId());
            sensorBean.create(2, "Humidade", "%", embalagemCartao.getId());
            sensorBean.create(3, "Luminosidade", "LUX", embalagemVidro.getId());
            sensorBean.create(4, "Pressao", "hPa", embalagemMetal.getId());
            sensorBean.create(5, "Velocidade do Vento", "km/h", embalagemPlastico.getId());
            sensorBean.create(6, "Direcao do Vento", "N", embalagemCartao.getId());
            sensorBean.create(7, "Precipitacao", "mm", embalagemVidro.getId());

            // Observacoes
            observacaoBean.create("20", 1L);
            observacaoBean.create("90", 2L);
            observacaoBean.create("400", 3L);
            observacaoBean.create("1000", 4L);


        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}
