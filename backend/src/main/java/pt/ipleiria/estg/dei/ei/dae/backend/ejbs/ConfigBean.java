package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;


import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

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

    private Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB() {
        System.out.println("ConfigBean: PostConstruct");

        try {
            fabricanteDeProdutosBean.create("Fabrica1", "123", "fabricante 1", "fabricante1@mail.pt");
            fabricanteDeProdutosBean.create("Fabrica2", "123", "fabricante 2", "fabricante2@mail.pt");

            produtoCatalogoBean.create("produto 1", "Fabrica1");
            produtoCatalogoBean.create("produto 2", "Fabrica2");

            operadorDeLogisticaBean.create("ValterLogo", "123", "Valte", "valtefutebole@mail.pt");
            operadorDeLogisticaBean.create("Joaoz", "123", "Joao", "joao@mail.pt");

            sensorBean.create(1,"Temperatura", "C");
            sensorBean.create(2,"Humidade",  "%");
            sensorBean.create(3,"Luminosidade",  "LUX");
            sensorBean.create(4, "Pressao",  "hPa");
            sensorBean.create(5, "Velocidade do Vento",  "km/h");
            sensorBean.create(6, "Direcao do Vento",  "N");
            sensorBean.create(7, "Precipitacao",  "mm");

            observacaoBean.create(java.time.LocalDate.now(), "20", 1L);
            observacaoBean.create(java.time.LocalDate.now(), "90", 2L);
            observacaoBean.create(java.time.LocalDate.now(), "400", 3L);
            observacaoBean.create(java.time.LocalDate.now(), "1000", 4L);



        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}
