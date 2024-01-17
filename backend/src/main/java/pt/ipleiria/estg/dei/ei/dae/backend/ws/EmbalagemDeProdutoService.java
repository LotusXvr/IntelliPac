package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EmbalagemDeProdutoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoFisicoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.EmbalagemDeProdutoBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.EmbalagemDeProduto;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.ProdutoFisico;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Path("embalagensDeProduto")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class EmbalagemDeProdutoService {

    @EJB
    private EmbalagemDeProdutoBean embalagemDeProdutoBean;

    private EmbalagemDeProdutoDTO toDTONoDetails(EmbalagemDeProduto embalagemDeProduto) {
        return new EmbalagemDeProdutoDTO(
                embalagemDeProduto.getId(),
                embalagemDeProduto.getMaterial(),
                embalagemDeProduto.getTipoEmbalagem(),
                embalagemDeProduto.getAltura(),
                embalagemDeProduto.getLargura(),
                embalagemDeProduto.getComprimento()
        );
    }

    private List<EmbalagemDeProdutoDTO> toDTOsNoDetails(List<EmbalagemDeProduto> embalagemDeProdutos) {
        return embalagemDeProdutos.stream().map(this::toDTONoDetails).collect(Collectors.toList());
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

        embalagemDeProdutoDTO.setSensores(sensorsToDTOs(embalagemDeProduto.getSensores()));
        embalagemDeProdutoDTO.setProdutos(produtosFisicosToDTOs(embalagemDeProduto.getProdutos()));
        return embalagemDeProdutoDTO;
    }

    private List<EmbalagemDeProdutoDTO> toDTOs(List<EmbalagemDeProduto> embalagemDeProdutos) {
        return embalagemDeProdutos.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private SensorDTO toDTO(Sensor sensor) {
        return new SensorDTO(
                sensor.getId(),
                sensor.getIdSensor(),
                sensor.getTipo(),
                sensor.getUnidade(),
                sensor.getEstado()
        );
    }

    private List<SensorDTO> sensorsToDTOs(List<Sensor> sensors) {
        return sensors.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private ProdutoFisicoDTO toDTO(ProdutoFisico produtoFisico) {

        // TODO: verificar se este ficou sem problema
        return new ProdutoFisicoDTO(
                produtoFisico.getId(),
                produtoFisico.getNomeProduto(),
                produtoFisico.getFabricante().getUsername(),
                produtoFisico.getProdutoCatalogo().getId(),
                produtoFisico.getEncomenda().getId()
        );

    }

    private List<ProdutoFisicoDTO> produtosFisicosToDTOs(List<ProdutoFisico> produtoFisicos) {
        return produtoFisicos.stream().map(this::toDTO).collect(Collectors.toList());
    }


    @GET
    @Path("/")
    public List<EmbalagemDeProdutoDTO> getAll() {
        return toDTOsNoDetails(embalagemDeProdutoBean.getAllEmbalagensDeProduto());
    }

    @POST
    @Path("/")
    public Response create(EmbalagemDeProdutoDTO embalagemDeProdutoDTO)
            throws Exception {
        EmbalagemDeProduto embalagemDeProduto = embalagemDeProdutoBean.create(
                embalagemDeProdutoDTO.getMaterial(),
                embalagemDeProdutoDTO.getAltura(),
                embalagemDeProdutoDTO.getLargura(),
                embalagemDeProdutoDTO.getComprimento(),
                embalagemDeProdutoDTO.getTipoEmbalagem()

        );
        return Response.status(Response.Status.CREATED).entity(toDTO(embalagemDeProduto)).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") long id, EmbalagemDeProdutoDTO embalagemDeProdutoDTO) throws MyEntityNotFoundException {
        var embalagemDeProduto = embalagemDeProdutoBean.find(id);
        if (embalagemDeProduto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        embalagemDeProdutoBean.update(id, embalagemDeProdutoDTO.getMaterial(), embalagemDeProduto.getTipoEmbalagem());
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteProdutoCatalogo(@PathParam("id") long id) throws MyEntityNotFoundException {
        embalagemDeProdutoBean.remove(id);
        return Response.ok().build();
    }

    @GET
    @Path("{id}")
    public Response getEmbalagemDeProdutoDetails(@PathParam("id") long id) throws MyEntityNotFoundException {
        EmbalagemDeProduto embalagemDeProduto = embalagemDeProdutoBean.getEmbalagemDeProdutoWithDetails(id);
        if (embalagemDeProduto != null) {
            System.out.println(embalagemDeProduto.getProdutos());
            return Response.ok(toDTO(embalagemDeProduto)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_STUDENT")
                .build();

    }
}
