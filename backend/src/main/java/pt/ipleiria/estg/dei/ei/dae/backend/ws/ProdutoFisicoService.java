package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EmbalagemDeProdutoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ObservacaoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoFisicoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.ProdutoFisicoBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.*;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backend.security.Authenticated;

import java.util.List;

@Path("produtosFisicos")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"FabricanteDeProdutos"})
public class ProdutoFisicoService {
    @EJB
    private ProdutoFisicoBean produtoFisicoBean;

    private ProdutoFisicoDTO toDTO(ProdutoFisico produtoFisico) {
        ProdutoFisicoDTO produtoFisicoDTO = new ProdutoFisicoDTO(
                produtoFisico.getId(),
                produtoFisico.getNomeProduto(),
                produtoFisico.getFabricante().getUsername(),
                produtoFisico.getProdutoCatalogo().getId(),
                produtoFisico.getEncomenda().getId(),
                produtoFisico.getPeso()
        );
        produtoFisicoDTO.setEmbalagensDeProduto(embalagemToDTOs(produtoFisico.getEmbalagensDeProduto()));
        return produtoFisicoDTO;
    }



    private ProdutoFisicoDTO toDTONoEmbalagens(ProdutoFisico produtoFisico) {
        return new ProdutoFisicoDTO(
                produtoFisico.getId(),
                produtoFisico.getNomeProduto(),
                produtoFisico.getFabricante().getUsername(),
                produtoFisico.getProdutoCatalogo().getId(),
                produtoFisico.getEncomenda().getId(),
                produtoFisico.getPeso()
        );
    }

    private EmbalagemDeProdutoDTO toDTO(EmbalagemDeProduto embalagemDeProduto) {
        EmbalagemDeProdutoDTO embalagemDeProdutoDTO = new EmbalagemDeProdutoDTO(
                embalagemDeProduto.getId(),
                embalagemDeProduto.getMaterial(),
                embalagemDeProduto.getTipoEmbalagem(),
                embalagemDeProduto.getAltura(),
                embalagemDeProduto.getLargura(),
                embalagemDeProduto.getComprimento()
        );
        embalagemDeProdutoDTO.setSensores(sensoresToDTOs(embalagemDeProduto.getSensores()));
        return embalagemDeProdutoDTO;
    }

    private List<EmbalagemDeProdutoDTO> embalagemToDTOs(List<EmbalagemDeProduto> embalagemDeProdutos) {
        return embalagemDeProdutos.stream().map(this::toDTO).collect(java.util.stream.Collectors.toList());
    }

    private List<ProdutoFisicoDTO> toDTOs(List<ProdutoFisico> produtosFisicos) {
        return produtosFisicos.stream().map(this::toDTO).collect(java.util.stream.Collectors.toList());
    }

    private List<ProdutoFisicoDTO> toDTOsNoEmbalagens(List<ProdutoFisico> produtosFisicos) {
        return produtosFisicos.stream().map(this::toDTONoEmbalagens).collect(java.util.stream.Collectors.toList());
    }

    private List<SensorDTO> sensoresToDTOs(List<Sensor> sensores) {
        return sensores.stream().map(this::toDTO).collect(java.util.stream.Collectors.toList());
    }

    private SensorDTO toDTO(Sensor sensor) {
        SensorDTO sensorDTO = new SensorDTO(
                sensor.getId(),
                sensor.getIdSensor(),
                sensor.getTipo(),
                sensor.getUnidade(),
                sensor.getEstado()
        );
        sensorDTO.setObservacoes(observacoesToDTO(sensor.getObservacoes()));
        return sensorDTO;
    }
    private List<ObservacaoDTO> observacoesToDTO(List<Observacao> observacoes) {
        return observacoes.stream().map(this::toDTO).collect(java.util.stream.Collectors.toList());
    }

    private ObservacaoDTO toDTO(Observacao observacao) {
        return new ObservacaoDTO(
                observacao.getId(),
                observacao.getTimestamp(),
                observacao.getValor(),
                observacao.getSensor().getIdSensor()
        );
    }

    @GET
    @Path("/")
    public List<ProdutoFisicoDTO> getAllProdutos() {
        return toDTOsNoEmbalagens(produtoFisicoBean.getAllProductsFisico());
    }

    @GET
    @Path("fabricante/{username}")
    public List<ProdutoFisicoDTO> getAllProdutosFromFabricante(@PathParam("username") String username) throws MyEntityNotFoundException {
        List<ProdutoFisico> produtoFisicos = produtoFisicoBean.getAllProductsFromFabricante(username);
        return toDTOs(produtoFisicos);
    }

    @GET
    @Path("{id}")
    public ProdutoFisicoDTO getProdutoFisicoDetails(@PathParam("id") long id) {
        ProdutoFisico produtoFisico = produtoFisicoBean.getProdutoWithEmbalagens(id);
        return toDTO(produtoFisico);
    }

    @POST
    @Path("/")
    public Response createNewProdutoFisico (ProdutoFisicoDTO produtoFisicoDTO) throws Exception {
        produtoFisicoBean.create(
                produtoFisicoDTO.getProdutoCatalogoId(),
                produtoFisicoDTO.getEncomendaId()
        );

        ProdutoFisico newProdutoFisico = produtoFisicoBean.find(produtoFisicoDTO.getId());
        if(newProdutoFisico == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        return  Response.status(Response.Status.CREATED).entity(toDTO(newProdutoFisico)).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteProdutoFisico(@PathParam("id") long id) throws MyEntityNotFoundException {
        ProdutoFisico produtoFisico = produtoFisicoBean.find(id);
        if(produtoFisico == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        produtoFisicoBean.remove(id);
        return Response.status(Response.Status.OK).build();

    }

    @PUT
    @Path("{id}")
    public Response updateProdutoFisico(@PathParam("id") long id, ProdutoFisicoDTO produtoFisicoDTO) {
        try {
            produtoFisicoBean.update(id, produtoFisicoDTO.getNome(), produtoFisicoDTO.getFabricanteUsername(), produtoFisicoDTO.getProdutoCatalogoId());
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

}
