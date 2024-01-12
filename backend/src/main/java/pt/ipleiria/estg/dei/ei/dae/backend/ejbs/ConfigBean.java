package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;


import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EncomendaDTO;
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
            clienteBean.create("Marco", "123", "Marco Nunes", "marco@mail.pt");

            ProdutoCatalogo produtoCatalogo1 = produtoCatalogoBean.create("produto 1", "Fabrica1");
            ProdutoCatalogo produtoCatalogo2 = produtoCatalogoBean.create("produto 2", "Fabrica2");

            EncomendaDTO encomendaDTO = new EncomendaDTO();
            encomendaDTO.setConsumidorFinal("Emanuel");
            encomendaDTO.setOperadorLogistica("ValterLogo");
            Encomenda encomenda1 = encomendaBean.create(encomendaDTO);

            produtoFisicoBean.create("produto1Fisico", "Fabrica1", produtoCatalogo1.getId(), encomenda1.getId());
            produtoFisicoBean.create("produto2Fisico", "Fabrica1", produtoCatalogo2.getId(), encomenda1.getId());

            // Embalagens
            embalagemBean.create("Plastico");
            embalagemBean.create("Cartao");
            embalagemBean.create("Vidro");
            embalagemBean.create("Metal");

            // Sensores
            sensorBean.create(1, "Temperatura", "ºC");
            sensorBean.create(2, "Humidade", "%");
            sensorBean.create(3, "Luminosidade", "LUX");
            sensorBean.create(4, "Pressao", "hPa");
            sensorBean.create(5, "Velocidade do Vento", "km/h");
            sensorBean.create(6, "Direcao do Vento", "N");
            sensorBean.create(7, "Precipitacao", "mm");

            // associar sensores a embalagens
            embalagemBean.associateSensorToEmbalagem(3L, 1L);
            embalagemBean.associateSensorToEmbalagem(4L, 2L);
            embalagemBean.associateSensorToEmbalagem(2L, 3L);
            embalagemBean.associateSensorToEmbalagem(1L, 4L);
            embalagemBean.associateSensorToEmbalagem(1L, 5L);
            embalagemBean.associateSensorToEmbalagem(1L, 6L);
            embalagemBean.associateSensorToEmbalagem(2L, 1L);
            embalagemBean.associateSensorToEmbalagem(3L, 1L);


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
