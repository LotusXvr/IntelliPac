package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EmbalagemDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ObservacaoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Embalagem;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Observacao;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Sensor;

import java.util.Collections;
import java.util.List;

@Path("sensores") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class SensorService {

    @EJB
    private SensorBean sensorBean;

    private SensorDTO toDTONoObservacoesNoEmbalagens(Sensor sensor) {
        return new SensorDTO(
                sensor.getId(),
                sensor.getIdSensor(),
                sensor.getTipo(),
                sensor.getUnidade(),
                sensor.getEstado()
        );
    }

    private List<SensorDTO> toDTOsNoObservacoes(List<Sensor> sensores) {
        return sensores.stream().map(this::toDTONoObservacoesNoEmbalagens).collect(java.util.stream.Collectors.toList());
    }

    private SensorDTO toDTO(Sensor sensor) {
        SensorDTO sensorDTO = new SensorDTO(
                sensor.getId(),
                sensor.getIdSensor(),
                sensor.getTipo(),
                sensor.getUnidade(),
                sensor.getEstado()
        );
        sensorDTO.setEmbalagens(embalagemToDTOs(sensor.getEmbalagens()));
        sensorDTO.setObservacoes(observacaoToDTOs(sensor.getObservacoes()));
        return sensorDTO;
    }

    private List<SensorDTO> toDTOs(List<Sensor> sensores) {
        return sensores.stream().map(this::toDTO).collect(java.util.stream.Collectors.toList());
    }

    private ObservacaoDTO observacaoToDTO(Observacao observacao) {
        return new ObservacaoDTO(
                observacao.getId(),
                observacao.getTimestamp(),
                observacao.getValor(),
                observacao.getSensor().getId()
        );
    }

    private List<ObservacaoDTO> observacaoToDTOs(List<Observacao> observacoes) {
        return observacoes.stream().map(this::observacaoToDTO).collect(java.util.stream.Collectors.toList());
    }

    private EmbalagemDTO embalagemToDTO(Embalagem embalagem) {
        return new EmbalagemDTO(
                embalagem.getId(),
                embalagem.getMaterial(),
                embalagem.getAltura(),
                embalagem.getLargura(),
                embalagem.getComprimento()
        );
    }

    private List<EmbalagemDTO> embalagemToDTOs(List<Embalagem> embalagens) {
        return embalagens.stream().map(this::embalagemToDTO).collect(java.util.stream.Collectors.toList());
    }

    @GET
    @Path("/") // means: the relative url path is “/api/sensores/”
    public List<SensorDTO> getAllSensores() {
        List<Sensor> sensores = sensorBean.getAll();
        for (Sensor sensor : sensores) {
            Collections.reverse(sensor.getObservacoes());
        }
        return toDTOs(sensores);
    }

    @GET
    @Path("/{id}")
    public Response getSensorDetails(@PathParam("id") long id) {
        Sensor sensor = sensorBean.findSensorDetails(id);
        Collections.reverse(sensor.getObservacoes());
        return Response.status(Response.Status.OK).entity(toDTO(sensor)).build();
    }


    @POST
    @Path("/")
    public Response createNewSensor(SensorDTO sensorDTO) {
        // check if idSensor has already been assigned
        /*if (sensorBean.findBySensorId(sensorDTO.getIdSensor()) != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }*/
        Sensor sensor = sensorBean.create(
                sensorDTO.getTipo(),
                sensorDTO.getUnidade()
        );
        /*Sensor sensor = sensorBean.findBySensorId(sensorDTO.getIdSensor());
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }*/
        return Response.status(Response.Status.CREATED).entity(toDTO(sensor)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateSensor(@PathParam("id") long id, SensorDTO sensorDTO) {
        try {
            sensorBean.update(
                    id,
                    sensorDTO.getIdSensor(),
                    sensorDTO.getTipo(),
                    sensorDTO.getUnidade()
            );
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSensor(@PathParam("id") long id) {
        try {
            sensorBean.remove(id);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/{id}/gerarObservacao")
    public Response gerarObservacao(@PathParam("id") long sensorId) {
        try {
            sensorBean.gerarObservacao(
                    sensorId
            );
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }


}
