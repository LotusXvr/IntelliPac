package pt.ipleiria.estg.dei.ei.dae.backend.ws;


import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EmbalagemDeTransporteDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.TipoEmbalagemDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.TipoSensorDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.TipoEmbalagemProdutoBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.EmbalagemDeTransporte;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.TipoEmbalagemProduto;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.TipoSensor;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backend.security.Authenticated;

import java.util.List;

@Path("tipoEmbalagens") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
@Authenticated
@RolesAllowed({"FabricanteDeProdutos"})
public class TipoEmbalagemService {

    @EJB
    private TipoEmbalagemProdutoBean tipoEmbalagemBean;

    private TipoEmbalagemDTO toDTONoSensores(TipoEmbalagemProduto tipoEmbalagem) {
        TipoEmbalagemDTO tipoEmbalagemDTO = new TipoEmbalagemDTO(
                tipoEmbalagem.getId(),
                tipoEmbalagem.getTipoEmbalagem(),
                tipoEmbalagem.getMaterial(),
                tipoEmbalagem.getAltura(),
                tipoEmbalagem.getLargura(),
                tipoEmbalagem.getComprimento()
        );
        return tipoEmbalagemDTO;
    }

    private List<TipoEmbalagemDTO> toDTOsNoSensores(List<TipoEmbalagemProduto> tipoEmbalagens) {
        return tipoEmbalagens.stream().map(this::toDTONoSensores).collect(java.util.stream.Collectors.toList());
    }

    private TipoEmbalagemDTO toDTO(TipoEmbalagemProduto tipoEmbalagem) {
        TipoEmbalagemDTO tipoEmbalagemDTO = new TipoEmbalagemDTO(
                tipoEmbalagem.getId(),
                tipoEmbalagem.getTipoEmbalagem(),
                tipoEmbalagem.getMaterial(),
                tipoEmbalagem.getAltura(),
                tipoEmbalagem.getLargura(),
                tipoEmbalagem.getComprimento()
        );
        tipoEmbalagemDTO.setSensoresDTO(toDTOsSensores(tipoEmbalagem.getTipoSensor()));
        return tipoEmbalagemDTO;
    }

    private List<TipoEmbalagemDTO> toDTOs(List<TipoEmbalagemProduto> tipoEmbalagens) {
        return tipoEmbalagens.stream().map(this::toDTO).collect(java.util.stream.Collectors.toList());
    }

    private TipoSensorDTO toDTO(TipoSensor tipoSensor) {
        return new TipoSensorDTO(
                tipoSensor.getId(),
                tipoSensor.getTipo(),
                tipoSensor.getUnidade(),
                tipoSensor.getEstado()
        );
    }

    private List<TipoSensorDTO> toDTOsSensores(List<TipoSensor> tipoSensors) {
        return tipoSensors.stream().map(this::toDTO).collect(java.util.stream.Collectors.toList());
    }



    @GET
    @Path("/")
    public List<TipoEmbalagemDTO> getAllTipoEmbalagem() {
        return toDTOsNoSensores(tipoEmbalagemBean.getAllTipoEmbalagem());
    }

    @GET
    @Path("/{id}")
    public TipoEmbalagemDTO getTipoEmbalagemDetails(@PathParam("id") long id) throws MyEntityNotFoundException {
        TipoEmbalagemProduto tipoEmbalagem = tipoEmbalagemBean.getEmbalagemWithSensores(id);
        return toDTO(tipoEmbalagem);
    }

    @POST
    @Path("/")
    public Response create(TipoEmbalagemDTO tipoEmbalagemDTO) throws Exception {
        TipoEmbalagemProduto tipoEmbalagemProduto = tipoEmbalagemBean.create(
                tipoEmbalagemDTO.getTipo(),
                tipoEmbalagemDTO.getMaterial(),
                tipoEmbalagemDTO.getAltura(),
                tipoEmbalagemDTO.getLargura(),
                tipoEmbalagemDTO.getComprimento()
        );
        return Response.status(Response.Status.CREATED).entity(toDTONoSensores(tipoEmbalagemProduto)).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") long id, TipoEmbalagemDTO tipoEmbalagemDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        TipoEmbalagemProduto tipoEmbalagemProduto = tipoEmbalagemBean.find(id);
        if (tipoEmbalagemProduto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        tipoEmbalagemBean.update(id, tipoEmbalagemDTO.getTipo(), tipoEmbalagemDTO.getMaterial(), tipoEmbalagemDTO.getAltura(), tipoEmbalagemDTO.getLargura(), tipoEmbalagemDTO.getComprimento());
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") long id) throws MyEntityNotFoundException {
        TipoEmbalagemProduto tipoEmbalagemProduto = tipoEmbalagemBean.find(id);
        if(tipoEmbalagemProduto == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        tipoEmbalagemBean.remove(id);
        return Response.ok().build();
    }

    @POST
    @Path("{id}/adicionarSensor")
    public Response adicionarSensor(@PathParam("id") long id, long tipoSensorId) throws Exception {
        tipoEmbalagemBean.addTipoSensor(id, tipoSensorId);
        return Response.ok().build();
    }

    @POST
    @Path("{id}/removerSensor")
    public Response removerSensor(@PathParam("id") long id, long tipoSensorId) throws Exception {
        tipoEmbalagemBean.removeTipoSensor(id, tipoSensorId);
        return Response.ok().build();
    }
}
