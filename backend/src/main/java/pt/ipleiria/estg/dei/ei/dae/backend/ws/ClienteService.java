package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ClienteDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.ClienteBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Cliente;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Path("/clientes")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ClienteService {

    @EJB
    private ClienteBean clienteBean;

    @Context
    private SecurityContext securityContext;

    private ClienteDTO toDTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getUsername(),
                cliente.getPassword(),
                cliente.getName(),
                cliente.getEmail()
        );
    }
    private List<ClienteDTO> toDTOs(List<Cliente> clientes) {
        return clientes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<ClienteDTO> getAllClientes() {
        return toDTOs(clienteBean.getAll());
    }

    @POST
    @Path("/")
    public void create(ClienteDTO clienteDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        if (clienteBean.exists(clienteDTO.getName())) {
            throw new MyEntityExistsException("cliente with name '" + clienteDTO.getName() + "' already exists");
        }
        clienteBean.create(
                clienteDTO.getUsername(),
                clienteDTO.getPassword(),
                clienteDTO.getName(),
                clienteDTO.getEmail()
        );
    }

    @GET
    @Path("{username}")
    public Response getClienteDetails(@PathParam("username") String username) {
        Cliente cliente = clienteBean.find(username);
        if (cliente != null) {
            return Response.ok(toDTO(cliente)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_CLIENTE")
                .build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteCliente(@PathParam("username") String username) throws MyEntityNotFoundException {
        clienteBean.remove(username);
        return Response.ok().build();
    }

    @PUT
    @Path("{username}")
    public Response updateCliente(@PathParam("username") String username, ClienteDTO clienteDTO)
            throws MyEntityNotFoundException {
        var cliente = clienteBean.find(username);
        if (cliente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        clienteBean.update(username, clienteDTO.getPassword(), clienteDTO.getName(), clienteDTO.getEmail());
        return Response.ok().build();
    }
}
