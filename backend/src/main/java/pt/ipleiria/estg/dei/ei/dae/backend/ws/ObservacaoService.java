package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ObservacaoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.ObservacaoBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Observacao;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;

@Path("observacoes") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class ObservacaoService {

    @EJB
    private ObservacaoBean observacaoBean;

    private ObservacaoDTO toDTO(Observacao observacao) {
        return new ObservacaoDTO(
                observacao.getId(),
                observacao.getTimestamp(),
                observacao.getValor(),
                observacao.getSensor().getId()
        );
    }

    private List<ObservacaoDTO> toDTOs(List<Observacao> observacoes) {
        return observacoes.stream().map(this::toDTO).collect(java.util.stream.Collectors.toList());
    }

    @GET
    @Path("/") // means: the relative url path is “/api/observacoes/”
    public List<ObservacaoDTO> getAllObservacoes() {
        return toDTOs(observacaoBean.getAll());
    }

    @GET
    @Path("{id}")
    public ObservacaoDTO getObservacaoDetails(@PathParam("id") long id) throws MyEntityNotFoundException {
        Observacao observacao = observacaoBean.find(id);
        return toDTO(observacao);
    }

    @POST
    @Path("/")
    public Response createNewObservacao(ObservacaoDTO observacaoDTO) {
        try {
            observacaoBean.create(
                    observacaoDTO.getValor(),
                    observacaoDTO.getSensorId()
            );

            return Response.status(Response.Status.CREATED).entity(observacaoDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("{id}")
    public Response updateObservacao(@PathParam("id") long id, ObservacaoDTO observacaoDTO) {
        try {
            observacaoBean.update(
                    id,
                    observacaoDTO.getValor(),
                    observacaoDTO.getSensorId()
            );

            return Response.status(Response.Status.CREATED).entity(observacaoDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }

    @DELETE
    @Path("{id}")
    public Response deleteObservacao(@PathParam("id") long id) {
        try {
            observacaoBean.remove(id);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


}
