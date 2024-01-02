package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Sensor;

import java.nio.file.ReadOnlyFileSystemException;
import java.util.List;

@Path("sensores") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class SensorService {

    @EJB
    private SensorBean sensorBean;

    private SensorDTO toDTO(Sensor sensor) {
        return new SensorDTO(
                sensor.getIdSensor(),
                sensor.getValor(),
                sensor.getTipo(),
                sensor.getUnidade()

        );
    }

    private List<SensorDTO> toDTOs(List<Sensor> sensors) {
        return sensors.stream().map(this::toDTO).collect(java.util.stream.Collectors.toList());
    }

    @GET
    @Path("/") // means: the relative url path is “/api/sensores/”
    public List<SensorDTO> getAllSensores() {
        return toDTOs(sensorBean.getAll());
    }

    @GET
    @Path("{idSensor}")
    public SensorDTO getSensorDetails(@PathParam("idSensor") long idSensor) {
        Sensor sensor = sensorBean.findBySensorId(idSensor);
        return toDTO(sensor);
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
                sensorDTO.getValor(),
                sensorDTO.getUnidade()
        );
        Sensor sensor = sensorBean.findBySensorId(sensorDTO.getIdSensor());
        if (sensor == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{idSensor}")
    public Response updateSensor(@PathParam("idSensor") long idSensor, SensorDTO sensorDTO) {
        try {
            sensorBean.update(sensorDTO.getIdSensor(), sensorDTO.getTipo(), sensorDTO.getUnidade(), sensorDTO.getValor());
            return Response.status(Response.Status.OK).build();
        } catch (
                Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ERROR_UPDATING_SUBJECT").build();
        }

    }

    @DELETE
    @Path("{idSensor}")
    public Response deleteSensor(@PathParam("idSensor") long idSensor) {
        try {
            sensorBean.remove(idSensor);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ERROR_DELETE_SENSOR").build();
        }
    }

}
