package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.nio.file.ReadOnlyFileSystemException;
import java.util.List;

@Path("sensores") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class SensorService {

    @EJB
    private SensorBean sensorBean;

    private SensorDTO toDTONoObservacoes(Sensor sensor) {
        return new SensorDTO(
                sensor.getId(),
                sensor.getIdSensor(),
                sensor.getTipo(),
                sensor.getUnidade()
        );
    }

    private List<SensorDTO> toDTOsNoObservacoes(List<Sensor> sensores) {
        return sensores.stream().map(this::toDTONoObservacoes).collect(java.util.stream.Collectors.toList());
    }

    private SensorDTO toDTO(Sensor sensor) {
        return new SensorDTO(
                sensor.getId(),
                sensor.getIdSensor(),
                sensor.getTipo(),
                sensor.getUnidade(),
                sensor.getObservacoes()
        );
    }

    private List<SensorDTO> toDTOs(List<Sensor> sensores) {
        return sensores.stream().map(this::toDTO).collect(java.util.stream.Collectors.toList());
    }

    @GET
    @Path("/") // means: the relative url path is “/api/sensores/”
    public List<SensorDTO> getAllSensores() {
        return toDTOsNoObservacoes(sensorBean.getAll());
    }

    @GET
    @Path("{id}")
    public Response getSensorDetails(@PathParam("id") long id) throws MyEntityNotFoundException {
        Sensor sensor = sensorBean.findSensorWithObservacoes(id);
        return Response.ok(toDTO(sensor)).build();
    }

    @POST
    @Path("/")
    public Response createNewSensor(SensorDTO sensorDTO) {
        // check if idSensor has already been assigned
        if (sensorBean.findBySensorId(sensorDTO.getIdSensor()) != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }
        sensorBean.create(
                sensorDTO.getIdSensor(),
                sensorDTO.getTipo(),
                sensorDTO.getUnidade()
        );
        Sensor sensor = sensorBean.findBySensorId(sensorDTO.getIdSensor());
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.CREATED).entity(toDTO(sensor)).build();
    }

    @PUT
    @Path("{id}")
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
    @Path("{id}")
    public Response deleteSensor(@PathParam("id") long id) {
        try {
            sensorBean.remove(id);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

}