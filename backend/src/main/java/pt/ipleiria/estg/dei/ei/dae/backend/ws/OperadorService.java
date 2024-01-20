package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.mail.MessagingException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EmailDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EncomendaDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.OperadorDeLogisticaDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.OperadorDeLogisticaBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.OperadorDeLogistica;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backend.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("/operadores") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class OperadorService {

    @EJB
    private OperadorDeLogisticaBean operadorDeLogisticaBean;

    @EJB
    private EmailBean emailBean;

    @Context
    private SecurityContext securityContext;

    private OperadorDeLogisticaDTO toDTONoEncoemndas(OperadorDeLogistica operadorDeLogistica) {
        return new OperadorDeLogisticaDTO(
                operadorDeLogistica.getUsername(),
                operadorDeLogistica.getPassword(),
                operadorDeLogistica.getName(),
                operadorDeLogistica.getEmail()
        );
    }
    private List<OperadorDeLogisticaDTO> toDTOsNoEncomendas(List<OperadorDeLogistica> operadoresDeLogistica) {
        return operadoresDeLogistica.stream().map(this::toDTONoEncoemndas).collect(Collectors.toList());
    }

    private OperadorDeLogisticaDTO toDTO(OperadorDeLogistica operadorDeLogistica) {
        OperadorDeLogisticaDTO operadorDeLogisticaDTO = new OperadorDeLogisticaDTO(
                operadorDeLogistica.getUsername(),
                operadorDeLogistica.getPassword(),
                operadorDeLogistica.getName(),
                operadorDeLogistica.getEmail()
        );
        operadorDeLogisticaDTO.setEncomendas(encomendaToDTOs(operadorDeLogistica.getEncomendas()));
        return operadorDeLogisticaDTO;
    }
    private List<OperadorDeLogisticaDTO> toDTOs(List<OperadorDeLogistica> operadoresDeLogistica) {
        return operadoresDeLogistica.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private EncomendaDTO toDTO(Encomenda encomenda) {
        return new EncomendaDTO(
                encomenda.getId(),
                encomenda.getConsumidorFinal().getUsername(),
                encomenda.getDataEncomenda(),
                encomenda.getOperadorLogistica().getUsername(),
                encomenda.getEstado());
    }
    private List<EncomendaDTO> encomendaToDTOs(List<Encomenda> encomendas) {
        return encomendas.stream().map(this::toDTO).collect(Collectors.toList());
    }
    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public List<OperadorDeLogisticaDTO> getAllOperadoresDeLogistica() {
        return toDTOsNoEncomendas(operadorDeLogisticaBean.getAll());
    }

    @POST
    @Path("/")
    public void create(OperadorDeLogisticaDTO operadorDeLogisticaDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {

        if (operadorDeLogisticaBean.exists(operadorDeLogisticaDTO.getName())) {
            throw new MyEntityExistsException("operadorDeLogistica with name '" + operadorDeLogisticaDTO.getName() + "' already exists");
        }
        operadorDeLogisticaBean.create(
                operadorDeLogisticaDTO.getUsername(),
                operadorDeLogisticaDTO.getPassword(),
                operadorDeLogisticaDTO.getName(),
                operadorDeLogisticaDTO.getEmail()
        );
    }

    @GET
    @Path("{username}")
    public Response getOperadorLogisticaDetails(@PathParam("username") String username) throws MyEntityNotFoundException {
        OperadorDeLogistica operadorDeLogistica = operadorDeLogisticaBean.getOperadorWithEncomendas(username);

        if (operadorDeLogistica != null) {
            return Response.ok(toDTO(operadorDeLogistica)).build();
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_STUDENT")
                .build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteOperadorLogistica(@PathParam("username") String username) throws MyEntityNotFoundException {
        OperadorDeLogistica operadorDeLogistica = operadorDeLogisticaBean.find(username);
        if(operadorDeLogistica == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        operadorDeLogisticaBean.remove(username);
        return Response.ok().build();
    }

    @PUT
    @Path("{username}")
    public Response updateOperadorLogistica(@PathParam("username") String username, OperadorDeLogisticaDTO operadorDeLogisticaDTO) throws MyEntityNotFoundException {
        var operador = operadorDeLogisticaBean.find(username);
        if (operador == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        operadorDeLogisticaBean.update(username, operadorDeLogisticaDTO.getName(), operadorDeLogisticaDTO.getEmail());
        return Response.ok().build();
    }

    @POST
    @Path("/{username}/email/send")
    public Response sendEmail(@PathParam("username") String username, EmailDTO email)
            throws MyEntityNotFoundException, MessagingException {
        OperadorDeLogistica operadorDeLogistica = operadorDeLogisticaBean.find(username);
        if (operadorDeLogistica == null) {
            throw new MyEntityNotFoundException("Operador Logistica with username '" + username
                    + "' not found in our records.");
        }
        emailBean.send(operadorDeLogistica.getEmail(), email.getSubject(), email.getMessage());
        return Response.status(Response.Status.OK).entity("E-mail sent").build();
    }
}
