package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;


import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EncomendaDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Startup
@Singleton
public class ConfigBean {

    private final Logger logger = Logger.getLogger("ejbs.ConfigBean");
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
    @EJB
    EmbalagemDeProdutoBean embalagemDeProdutoBean;
    @EJB
    EmbalagemDeTransporteBean embalagemDeTransporteBean;
    @EJB
    TipoEmbalagemProdutoBean tipoEmbalagemProdutoBean;
    @EJB
    private ProdutoCatalogoBean produtoCatalogoBean;

    @PostConstruct
    public void populateDB() {
        System.out.println("ConfigBean: PostConstruct");

        try {
            FabricanteDeProdutos fabricanteApple = fabricanteDeProdutosBean.create("Apple", "123", "fabricante 1", "fabricante1@mail.pt");
            FabricanteDeProdutos fabricanteSamsung = fabricanteDeProdutosBean.create("Samsung", "123", "fabricante 2", "fabricante2@mail.pt");
            FabricanteDeProdutos fabricanteNacional = fabricanteDeProdutosBean.create("Nacional", "123", "fabricante 3", "fabricante3@mail.pt");

            OperadorDeLogistica operadorAmazon = operadorDeLogisticaBean.create("Amazon", "123", "Valte", "amazon@mail.pt");
            OperadorDeLogistica operadorEbay = operadorDeLogisticaBean.create("Ebay", "123", "Joao", "ebay@mail.pt");
            OperadorDeLogistica operadorContinente = operadorDeLogisticaBean.create("Continente", "123", "Joao", "continente@mail.pt");
            OperadorDeLogistica operadorPullBear = operadorDeLogisticaBean.create("Pull and Bear", "123", "Joao", "pullandbear@mail.pt");
            OperadorDeLogistica operadorHM = operadorDeLogisticaBean.create("H&M", "123", "Joao", "hm@mail.pt");

            clienteBean.create("Emanuel", "123", "Emanuel Nunes", "emanuel@nunes.pt");
            clienteBean.create("Marco", "123", "Marco Nunes", "marco@mail.pt");

            ProdutoCatalogo produtoCatalogoIphone = produtoCatalogoBean.create("iPhone 15", fabricanteApple.getUsername(), 10);
            ProdutoCatalogo produtoCatalogoArroz = produtoCatalogoBean.create("Arroz", fabricanteNacional.getUsername(), 6);
            produtoCatalogoBean.create("Cereais", fabricanteNacional.getUsername(), 8);
            produtoCatalogoBean.create("Máquina de Lavar", fabricanteSamsung.getUsername(), 12);
            produtoCatalogoBean.create("Secador", fabricanteSamsung.getUsername(), 14);
            produtoCatalogoBean.create("Teclado", fabricanteApple.getUsername(), 3);


            // Encomendas
            EncomendaDTO encomendaDTO = new EncomendaDTO();
            encomendaDTO.setConsumidorFinal("Emanuel");
            encomendaDTO.setOperadorLogistica(operadorAmazon.getUsername());
            Encomenda encomenda1 = encomendaBean.create(encomendaDTO);
            ProdutoFisico produtoFisico1 = produtoFisicoBean.create(produtoCatalogoIphone.getNomeProduto(), fabricanteSamsung.getUsername(), produtoCatalogoIphone.getId(), encomenda1.getId());
            produtoFisicoBean.create(produtoCatalogoArroz.getNomeProduto(), fabricanteNacional.getUsername(), produtoCatalogoArroz.getId(), encomenda1.getId());
            

            encomendaDTO = new EncomendaDTO();
            encomendaDTO.setConsumidorFinal("Marco");
            encomendaDTO.setOperadorLogistica(operadorHM.getUsername());
            Encomenda encomenda2 = encomendaBean.create(encomendaDTO);
            produtoFisicoBean.create(produtoCatalogoIphone.getNomeProduto(), fabricanteSamsung.getUsername(), produtoCatalogoIphone.getId(), encomenda2.getId());
            produtoFisicoBean.create(produtoCatalogoIphone.getNomeProduto(), fabricanteSamsung.getUsername(), produtoCatalogoArroz.getId(), encomenda2.getId());
            encomendaBean.patchEstado(encomenda2.getId(), "ENTREGUE");


            // Embalagens
            embalagemBean.create("Plastico");
            embalagemBean.create("Cartao");
            embalagemBean.create("Vidro");
            embalagemBean.create("Metal");

            // Sensores
            Sensor sensor1 = sensorBean.create(1, "Temperatura", "ºC");
            sensorBean.create(2, "Humidade", "%");
            sensorBean.create(3, "Luminosidade", "LUX");
            sensorBean.create(4, "Pressao", "hPa");
            sensorBean.create(5, "Localizacao", "GPS");
            sensorBean.create(6, "Danificado", "Boolean");

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

            //EmbalagensDeProduto
            EmbalagemDeProduto embalagemDeProdutoPlastico = embalagemDeProdutoBean.create("Plastico", 1);
            EmbalagemDeProduto embalagemDeProdutoMetal = embalagemDeProdutoBean.create("Metal", 2);
            EmbalagemDeProduto embalagemDeProdutoCartao = embalagemDeProdutoBean.create("Cartão", 3);
            EmbalagemDeProduto embalagemDeProdutoVidro = embalagemDeProdutoBean.create("Vidro", 2);

            EmbalagemDeTransporte embalagemDeTransporteCartao = embalagemDeTransporteBean.create("Cartão");
            EmbalagemDeTransporte embalagemDeTransporteVidro = embalagemDeTransporteBean.create("Vidro");
            EmbalagemDeTransporte embalagemDeTransportePlastico = embalagemDeTransporteBean.create("Plastico");
            EmbalagemDeTransporte embalagemDeTransporteEcoPlastico = embalagemDeTransporteBean.create("EcoPlastico");

            embalagemDeProdutoBean.associateSensorToEmbalagem(embalagemDeProdutoPlastico.getId(), sensor1.getId());
            embalagemDeProdutoBean.addProdutoToEmbalagem(embalagemDeProdutoPlastico.getId(), produtoFisico1.getId());
            embalagemDeTransporteBean.associateSensorToEmbalagem(embalagemDeTransporteCartao.getId(), sensor1.getId());
            embalagemDeTransporteBean.addEncomendaToEmbalagem(embalagemDeTransporteCartao.getId(), encomenda1.getId());
            embalagemDeTransporteBean.addEncomendaToEmbalagem(embalagemDeTransporteVidro.getId(), encomenda2.getId());


            List<Long> tipos = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

            List<String> materiais = new ArrayList<>(Arrays.asList("Plastico", "Metal", "Vidro", "Cartão"));

            for (long tipo : tipos
            ) {
                for (String material : materiais
                ) {
                    tipoEmbalagemProdutoBean.create(tipo, material);
                }
            }

            produtoCatalogoBean.addTipoEmbalagem(1, produtoCatalogoIphone.getId());
            produtoCatalogoBean.addTipoEmbalagem(8, produtoCatalogoIphone.getId());
            produtoCatalogoBean.addTipoEmbalagem(1, produtoCatalogoArroz.getId());
            produtoCatalogoBean.addTipoEmbalagem(5, produtoCatalogoArroz.getId());


        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}
