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

            sensorBean.create(1,"Temperatura", "20", "C");
            sensorBean.create(2,"Humidade", "50", "%");
            sensorBean.create(3,"Luminosidade", "100", "LUX");
            sensorBean.create(4, "Pressao", "1000", "hPa");
            sensorBean.create(5, "Velocidade do Vento", "10", "km/h");
            sensorBean.create(6, "Direcao do Vento", "Norte", "N");
            sensorBean.create(7, "Precipitacao", "0", "mm");




        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}
