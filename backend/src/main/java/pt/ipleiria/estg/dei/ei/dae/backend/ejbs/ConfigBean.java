package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;


import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EncomendaDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoCatalogoDTO;
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
    TipoSensorBean tipoSensorBean;
    @EJB
    private ProdutoCatalogoBean produtoCatalogoBean;

    @PostConstruct
    public void populateDB() {
        System.out.println("ConfigBean: PostConstruct");

        try {
            FabricanteDeProdutos fabricanteApple = fabricanteDeProdutosBean.create("Apple", "123", "Apple Silicon", "apple@mail.pt");
            FabricanteDeProdutos fabricanteSamsung = fabricanteDeProdutosBean.create("Samsung", "123", "Samsung Enterprise", "samsung@mail.pt");
            FabricanteDeProdutos fabricanteNacional = fabricanteDeProdutosBean.create("Nacional", "123", "É Nacional", "nacional@mail.pt");

            OperadorDeLogistica operadorAmazon = operadorDeLogisticaBean.create("Amazon", "123", "Valte", "amazon@mail.pt");
            OperadorDeLogistica operadorEbay = operadorDeLogisticaBean.create("Ebay", "123", "Joao", "ebay@mail.pt");
            OperadorDeLogistica operadorContinente = operadorDeLogisticaBean.create("Continente", "123", "Joao", "continente@mail.pt");
            OperadorDeLogistica operadorPullBear = operadorDeLogisticaBean.create("Pull and Bear", "123", "Joao", "pullandbear@mail.pt");
            OperadorDeLogistica operadorHM = operadorDeLogisticaBean.create("H&M", "123", "Joao", "hm@mail.pt");

            clienteBean.create("Emanuel", "123", "Emanuel Nunes", "emanuel@nunes.pt");
            clienteBean.create("Marco", "123", "Marco Oliveira", "marco@mail.pt");
            clienteBean.create("Joao", "123", "Joao Silva", "joao@mail.pt");
            clienteBean.create("Ricardo", "123", "Ricardo Santos", "ricardo@mail.pt");
            clienteBean.create("Pedro", "123", "Pedro Santos", "pedro@mail.pt");
            clienteBean.create("Maria", "123", "Maria Ferreira", "maria@mail.pt");
            clienteBean.create("Ana", "123", "Ana João", "ana@mail.pt");
            clienteBean.create("Marta", "123", "Marta Silva", "marta@mail.pt");


            //Produtos
            ProdutoCatalogoDTO produtoCatalogoDTOiPhone = new ProdutoCatalogoDTO();
            produtoCatalogoDTOiPhone.setNome("iPhone 15");
            produtoCatalogoDTOiPhone.setFabricanteUsername(fabricanteApple.getUsername());
            produtoCatalogoDTOiPhone.setPeso(10);
            ProdutoCatalogo produtoCatalogoIphone = produtoCatalogoBean.create(produtoCatalogoDTOiPhone);

            ProdutoCatalogoDTO produtoCatalogoDTOArroz = new ProdutoCatalogoDTO();
            produtoCatalogoDTOArroz.setNome("Arroz");
            produtoCatalogoDTOArroz.setFabricanteUsername(fabricanteNacional.getUsername());
            produtoCatalogoDTOArroz.setPeso(10);
            ProdutoCatalogo produtoCatalogoArroz = produtoCatalogoBean.create(produtoCatalogoDTOArroz);

            ProdutoCatalogoDTO produtoCatalogoDTOCereais = new ProdutoCatalogoDTO();
            produtoCatalogoDTOCereais.setNome("Cereais");
            produtoCatalogoDTOCereais.setFabricanteUsername(fabricanteNacional.getUsername());
            produtoCatalogoDTOCereais.setPeso(10);
            ProdutoCatalogo produtoCatalogoCereais = produtoCatalogoBean.create(produtoCatalogoDTOCereais);

            ProdutoCatalogoDTO produtoCatalogoDTOMaquinaLavar = new ProdutoCatalogoDTO();
            produtoCatalogoDTOMaquinaLavar.setNome("Máquina de Lavar");
            produtoCatalogoDTOMaquinaLavar.setFabricanteUsername(fabricanteSamsung.getUsername());
            produtoCatalogoDTOMaquinaLavar.setPeso(10);
            ProdutoCatalogo produtoCatalogoMaquinaLavar = produtoCatalogoBean.create(produtoCatalogoDTOMaquinaLavar);

            ProdutoCatalogoDTO produtoCatalogoDTOSecador = new ProdutoCatalogoDTO();
            produtoCatalogoDTOSecador.setNome("Secador");
            produtoCatalogoDTOSecador.setFabricanteUsername(fabricanteSamsung.getUsername());
            produtoCatalogoDTOSecador.setPeso(10);
            ProdutoCatalogo produtoCatalogoSecador = produtoCatalogoBean.create(produtoCatalogoDTOSecador);

            ProdutoCatalogoDTO produtoCatalogoDTOFogao = new ProdutoCatalogoDTO();
            produtoCatalogoDTOFogao.setNome("Fogão");
            produtoCatalogoDTOFogao.setFabricanteUsername(fabricanteSamsung.getUsername());
            produtoCatalogoDTOFogao.setPeso(10);
            ProdutoCatalogo produtoCatalogoFogao = produtoCatalogoBean.create(produtoCatalogoDTOFogao);


            // Encomendas
            EncomendaDTO encomendaDTO = new EncomendaDTO();
            encomendaDTO.setConsumidorFinal("Emanuel");
            encomendaDTO.setOperadorLogistica(operadorAmazon.getUsername());
            Encomenda encomenda1 = encomendaBean.create(encomendaDTO);
            ProdutoFisico produtoFisico1 = produtoFisicoBean.create(produtoCatalogoIphone.getId(), encomenda1.getId());
            produtoFisicoBean.create(produtoCatalogoArroz.getId(), encomenda1.getId());


            encomendaDTO = new EncomendaDTO();
            encomendaDTO.setConsumidorFinal("Marco");
            encomendaDTO.setOperadorLogistica(operadorHM.getUsername());
            Encomenda encomenda2 = encomendaBean.create(encomendaDTO);
            produtoFisicoBean.create( produtoCatalogoIphone.getId(), encomenda2.getId());
            produtoFisicoBean.create( produtoCatalogoArroz.getId(), encomenda2.getId());
            encomendaBean.patchEstado(encomenda2.getId(), "ENTREGUE");


            // Embalagens
            embalagemBean.create("Plastico", 150, 10, 40);
            embalagemBean.create("Cartao", 150, 10, 40);
            embalagemBean.create("Vidro", 150, 10, 40);
            embalagemBean.create("Metal", 150, 10, 40);

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
            EmbalagemDeProduto embalagemDeProdutoPlastico = embalagemDeProdutoBean.create("Plastico", 40, 100, 150,1);
            EmbalagemDeProduto embalagemDeProdutoMetal = embalagemDeProdutoBean.create("Metal",20, 50, 50, 2);
            EmbalagemDeProduto embalagemDeProdutoCartao = embalagemDeProdutoBean.create("Cartão",10, 10, 40, 3);
            EmbalagemDeProduto embalagemDeProdutoVidro = embalagemDeProdutoBean.create("Vidro",20, 20, 20, 2);

            EmbalagemDeTransporte embalagemDeTransporteCartao = embalagemDeTransporteBean.create("Cartão",150, 150, 150);
            EmbalagemDeTransporte embalagemDeTransporteVidro = embalagemDeTransporteBean.create("Vidro", 200, 200, 200);
            EmbalagemDeTransporte embalagemDeTransportePlastico = embalagemDeTransporteBean.create("Plastico",50, 50, 50);
            EmbalagemDeTransporte embalagemDeTransporteEcoPlastico = embalagemDeTransporteBean.create("EcoPlastico", 5000, 5000, 500);

            //embalagemDeProdutoBean.associateSensorToEmbalagem(embalagemDeProdutoPlastico.getId(), sensor1.getId());
            embalagemDeProdutoBean.addProdutoToEmbalagem(embalagemDeProdutoPlastico.getId(), produtoFisico1.getId());
            embalagemDeTransporteBean.associateSensorToEmbalagem(embalagemDeTransporteCartao.getId(), sensor1.getId());
            embalagemDeTransporteBean.removeSensorFromEmbalagem(embalagemDeTransporteCartao.getId(), sensor1.getId());
            embalagemDeTransporteBean.associateSensorToEmbalagem(embalagemDeTransporteCartao.getId(), sensor1.getId()); 
            embalagemDeTransporteBean.addEncomendaToEmbalagem(embalagemDeTransporteCartao.getId(), encomenda1.getId());
            embalagemDeTransporteBean.addEncomendaToEmbalagem(embalagemDeTransporteVidro.getId(), encomenda2.getId());


            List<Long> tipos = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

            List<String> materiais = new ArrayList<>(Arrays.asList("Plastico", "Metal", "Vidro", "Cartão"));

            TipoSensor tipoSensorDanificado = tipoSensorBean.create("Danificado", "Boolean");

            for (long tipo : tipos
            ) {
                for (String material : materiais
                ) {
                    TipoEmbalagemProduto tipoEmbalagem = tipoEmbalagemProdutoBean.create(tipo, material, 100,100,100);

                    tipoEmbalagemProdutoBean.addTipoSensor(tipoEmbalagem.getId(), tipoSensorDanificado.getId());
                }
            }

            TipoSensor testeSensor = tipoSensorBean.create("Vento", "ºC");

            tipoEmbalagemProdutoBean.addTipoSensor(1, testeSensor.getId());

            produtoCatalogoBean.addTipoEmbalagem(1, produtoCatalogoIphone.getId());
            produtoCatalogoBean.addTipoEmbalagem(8, produtoCatalogoIphone.getId());
            produtoCatalogoBean.addTipoEmbalagem(1, produtoCatalogoArroz.getId());
            produtoCatalogoBean.addTipoEmbalagem(5, produtoCatalogoArroz.getId());

            produtoFisicoBean.create( produtoCatalogoIphone.getId(), encomenda1.getId());


        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}
