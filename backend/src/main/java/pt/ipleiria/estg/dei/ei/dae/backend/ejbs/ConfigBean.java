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
            fabricanteDeProdutosBean.create("fabricante 1");
            fabricanteDeProdutosBean.create("fabricante 2");

            produtoBean.createProduto("produto 1", 1);
            produtoBean.createProduto("produto 2", 1);

        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}
