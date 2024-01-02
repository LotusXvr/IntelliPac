package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Sensor;

import java.util.List;

@Path("sensores") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class SensorService {

    @EJB
    private SensorBean sensorBean;

    private SensorDTO toDTO(Sensor sensor) {
        return new SensorDTO(
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

}
