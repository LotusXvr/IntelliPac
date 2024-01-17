package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EmbalagemDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.EmbalagemBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Embalagem;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Path("embalagens") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class EmbalagemService {

    @EJB
    private EmbalagemBean embalagemBean;

    private EmbalagemDTO toDTOSnoSensor(Embalagem embalagem) {
        return new EmbalagemDTO(
                embalagem.getId(),
                embalagem.getMaterial(),
                embalagem.getAltura(),
                embalagem.getLargura(),
                embalagem.getComprimento()
        );
    }

    private List<EmbalagemDTO> toDTOSnoSensor(List<Embalagem> embalagens) {
        return embalagens.stream().map(this::toDTOSnoSensor).collect(java.util.stream.Collectors.toList());
    }

    private EmbalagemDTO toDTO(Embalagem embalagem) {
        return new EmbalagemDTO(
                embalagem.getId(),
                embalagem.getMaterial(),
                embalagem.getAltura(),
                embalagem.getLargura(),
                embalagem.getComprimento(),
                sensorToDTOs(embalagem.getSensores())
        );
    }

    private List<EmbalagemDTO> toDTOs(List<Embalagem> embalagens) {
        return embalagens.stream().map(this::toDTO).collect(java.util.stream.Collectors.toList());
    }

    private SensorDTO sensorToDTO(Sensor sensor) {
        return new SensorDTO(
                sensor.getId(),
                sensor.getIdSensor(),
                sensor.getTipo(),
                sensor.getUnidade(),
                sensor.getEstado()
        );
    }

    private List<SensorDTO> sensorToDTOs(List<Sensor> sensores) {
        return sensores.stream()
                .map(this::sensorToDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<EmbalagemDTO> getAllEmbalagens() {
        return toDTOSnoSensor(embalagemBean.getAll());
    }


    @GET
    @Path("/{id}")
    public Response getEmbalagemDetails(@PathParam("id") long id) throws MyEntityNotFoundException {
        Embalagem embalagem = embalagemBean.findEmbalagemDetails(id);

        return Response.status(Response.Status.OK).entity(toDTO(embalagem)).build();
    }

    @POST
    @Path("/")
    public Response createNewEmbalagem(EmbalagemDTO embalagemDTO) throws MyEntityNotFoundException {
        embalagemBean.create(embalagemDTO.getMaterial(), embalagemDTO.getAltura(), embalagemDTO.getLargura(), embalagemDTO.getComprimento());
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateEmbalagem(@PathParam("id") long id, EmbalagemDTO embalagemDTO) throws MyEntityNotFoundException {
        embalagemBean.update(id, embalagemDTO.getMaterial());
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEmbalagem(@PathParam("id") long id) throws MyEntityNotFoundException {
        embalagemBean.delete(id);
        return Response.status(Response.Status.OK).build();
    }


}
