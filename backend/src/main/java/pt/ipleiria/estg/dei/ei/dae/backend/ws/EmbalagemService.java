package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EmbalagemDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.EmbalagemBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Embalagem;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Path("sensores") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class EmbalagemService {

    @EJB
    private EmbalagemBean embalagemBean;

    private EmbalagemDTO toDTONoSensores(Embalagem embalagem) {
        return new EmbalagemDTO(
                embalagem.getId(),
                embalagem.getMaterial()
        );
    }

    private List<EmbalagemDTO> toDTOsNoSensores(List<Embalagem> embalagens) {
        return embalagens.stream().map(this::toDTONoSensores).collect(java.util.stream.Collectors.toList());
    }

    private EmbalagemDTO toDTO(Embalagem embalagem) {
        return new EmbalagemDTO(
                embalagem.getId(),
                embalagem.getMaterial(),
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
                sensor.getUnidade()
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
        return toDTOsNoSensores(embalagemBean.getAll());
    }

    @GET
    @Path("/{id}")
    public EmbalagemDTO getEmbalagemDetails(long id) throws MyEntityNotFoundException {
        return toDTO(embalagemBean.find(id));
    }








}
