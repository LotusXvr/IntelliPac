package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EmbalagemDeProdutoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EmbalagemDeTransporteDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EncomendaDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.EmbalagemDeProdutoBean;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.EmbalagemDeTransporteBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.EmbalagemDeProduto;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.EmbalagemDeTransporte;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Path("embalagensDeTransporte")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class EmbalagemDeTransporteService {

    @EJB
    private EmbalagemDeTransporteBean embalagemDeTransporteBean;

    private EmbalagemDeTransporteDTO toDTONoDetails(EmbalagemDeTransporte embalagemDeTransporte) {
        return new EmbalagemDeTransporteDTO(
                embalagemDeTransporte.getId(),
                embalagemDeTransporte.getMaterial()
        );
    }

    private List<EmbalagemDeTransporteDTO> toDTOsNoDetails(List<EmbalagemDeTransporte> embalagemDeTransportes) {
        return embalagemDeTransportes.stream().map(this::toDTONoDetails).collect(Collectors.toList());
    }

    private EmbalagemDeTransporteDTO toDTO(EmbalagemDeTransporte embalagemDeTransporte) {
        EmbalagemDeTransporteDTO embalagemDeTransporteDTO = new EmbalagemDeTransporteDTO(
                embalagemDeTransporte.getId(),
                embalagemDeTransporte.getMaterial()
        );

        embalagemDeTransporteDTO.setSensorDTOS(sensorsToDTOs(embalagemDeTransporte.getSensores()));
        embalagemDeTransporteDTO.setEncomendas(encomendaToDTOs(embalagemDeTransporte.getEncomendas()));
        return  embalagemDeTransporteDTO;
    }

    private List<EmbalagemDeTransporteDTO> toDTOs(List<EmbalagemDeTransporte> embalagemDeTransportes) {
        return embalagemDeTransportes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private SensorDTO toDTO(Sensor sensor){
        return new SensorDTO(
                sensor.getId(),
                sensor.getIdSensor(),
                sensor.getTipo(),
                sensor.getUnidade()
        );
    }

    private List<SensorDTO> sensorsToDTOs (List<Sensor> sensors) {
        return sensors.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private EncomendaDTO toDTO(Encomenda encomenda){
        return new EncomendaDTO(
                encomenda.getId(),
                encomenda.getConsumidorFinal().getUsername(),
                encomenda.getDataEncomenda(),
                encomenda.getOperadorLogistica().getUsername(),
                encomenda.getEstado()
        );
    }

    private List<EncomendaDTO> encomendaToDTOs (List<Encomenda> encomendas) {
        return encomendas.stream().map(this::toDTO).collect(Collectors.toList());
    }


    @GET
    @Path("/")
    public List<EmbalagemDeTransporteDTO> getAll() {
        return toDTOsNoDetails(embalagemDeTransporteBean.getAllEmbalagensDeTransporte());
    }

    @GET
    @Path("{id}")
    public Response getEmbalagemDeTransporteDetails(@PathParam("id") long id) throws MyEntityNotFoundException {
        EmbalagemDeTransporte embalagemDeTransporte = embalagemDeTransporteBean.getEmbalagensDeTransporteDetails(id);
        if (embalagemDeTransporte != null) {
            return Response.ok(toDTO(embalagemDeTransporte)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_STUDENT")
                .build();
    }

    @POST
    @Path("/")
    public Response create(EmbalagemDeTransporteDTO embalagemDeTransporteDTO)
            throws Exception {
        EmbalagemDeTransporte embalagemDeTransporte = embalagemDeTransporteBean.create(
                embalagemDeTransporteDTO.getMaterial()
        );
        return Response.status(Response.Status.CREATED).entity(toDTO(embalagemDeTransporte)).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") long id, EmbalagemDeTransporteDTO embalagemDeTransporteDTO) throws MyEntityNotFoundException {
        EmbalagemDeTransporte embalagemDeTransporte = embalagemDeTransporteBean.find(id);
        if (embalagemDeTransporte == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        embalagemDeTransporteBean.update(id, embalagemDeTransporteDTO.getMaterial());
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteProdutoCatalogo(@PathParam("id") long id) throws MyEntityNotFoundException {
        embalagemDeTransporteBean.remove(id);
        return Response.ok().build();
    }
}