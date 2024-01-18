package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.TipoEmbalagemDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.TipoSensorDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.TipoEmbalagemProdutoBean;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.TipoSensorBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.TipoEmbalagemProduto;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.TipoSensor;

import java.util.List;

@Path("tipoSensor") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class TipoSensorService {

    @EJB
    private TipoSensorBean tipoSensorBean;

    private TipoSensorDTO toDTO(TipoSensor tipoSensor) {
        return new TipoSensorDTO(
                tipoSensor.getId(),
                tipoSensor.getTipo(),
                tipoSensor.getUnidade(),
                tipoSensor.getEstado()
        );
    }

    private List<TipoSensorDTO> toDTOs(List<TipoSensor> tipoSensors) {
        return tipoSensors.stream().map(this::toDTO).collect(java.util.stream.Collectors.toList());
    }

    @GET
    @Path("/")
    public List<TipoSensorDTO> getAllTipoEmbalagem() {
        return toDTOs(tipoSensorBean.getAllTipoSensor());
    }

}
