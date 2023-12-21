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
    private ProdutoBean produtoBean;

    @EJB
    FabricanteDeProdutosBean fabricanteDeProdutosBean;

    private Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB() {
        System.out.println("ConfigBean: PostConstruct");

        try {
            fabricanteDeProdutosBean.create("fabricante1", "123", "fabricante 1", "fabricante1@mail.pt");
            fabricanteDeProdutosBean.create("fabricante2", "123", "fabricante 2", "fabricante2@mail.pt");

            produtoBean.create("produto 1", 1);
            produtoBean.create("produto 2", 1);

        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}
