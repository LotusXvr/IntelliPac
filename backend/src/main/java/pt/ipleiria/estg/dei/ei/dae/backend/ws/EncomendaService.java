package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EmbalagemDeTransporteDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EncomendaDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoFisicoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.EncomendaBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.EmbalagemDeTransporte;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.ProdutoFisico;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Path("encomendas")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class EncomendaService {

    @EJB
    private EncomendaBean encomendaBean;

    @Context
    private SecurityContext securityContext;

    // toDTO
    private EncomendaDTO toDTO(Encomenda encomenda) {
        return new EncomendaDTO(
                // Long id, String consumidorFinal, Date dataEncomenda, List<ProdutoDTO> produtos
                encomenda.getId(),
                encomenda.getConsumidorFinal().getUsername(),
                encomenda.getDataEncomenda(),
                produtosToDTOs(encomenda.getProdutos()),
                embalagensTransporteToDTOs(encomenda.getEmbalagensTransporte()),
                encomenda.getOperadorLogistica().getUsername(),
                encomenda.getEstado()
        );
    }

    // toDTOs
    private List<EncomendaDTO> toDTOs(List<Encomenda> encomendas) {
        return encomendas.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // produtosToDTOs
    private List<ProdutoFisicoDTO> produtosToDTOs(List<ProdutoFisico> produtos) {
        return produtos.stream().map(this::toDTO).collect(Collectors.toList());
    }
    private List<EmbalagemDeTransporteDTO> embalagensTransporteToDTOs(List<EmbalagemDeTransporte> embalagemTransportes) {
        return embalagemTransportes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private EmbalagemDeTransporteDTO toDTO(EmbalagemDeTransporte embalagemDeTransporte) {
        return new EmbalagemDeTransporteDTO(
                embalagemDeTransporte.getId(),
                embalagemDeTransporte.getMaterial(),
                toDTOs(embalagemDeTransporte.getEncomendas())
        );
    }

    // toDTO
    private ProdutoFisicoDTO toDTO(ProdutoFisico produto) {
        return new ProdutoFisicoDTO(
                produto.getId(),
                produto.getNomeProduto(),
                produto.getFabricante().getUsername(),
                produto.getProdutoCatalogo().getId(),  // Adicione esta linha para obter o produtoCatalogoId
                produto.getEncomenda().getId()        // Adicione esta linha para obter o encomendaId
        );
    }


    // getAllEncomendas
    @GET
    @Path("/")
    public List<EncomendaDTO> getAllEncomendas() {
        return toDTOs(encomendaBean.getAllEncomendas());
    }

    // getEncomendaById
    @GET
    @Path("{id}")
    public Response getEncomendaById(@PathParam("id") long id) throws MyEntityNotFoundException {
        Encomenda encomenda = encomendaBean.getEncomendaById(id);
        return Response.status(Response.Status.OK).entity(toDTO(encomenda)).build();
    }

    // get encomendas by estado
    @GET
    @Path("/estado/{estado}")
    public List<EncomendaDTO> getEncomendasByEstado(@PathParam("estado") String estado) throws MyEntityNotFoundException {
        return toDTOs(encomendaBean.getEncomendasByEstado(estado));
    }

    // POST
    // createEncomenda (cliente)
    // Encomenda create(String consumidorFinal, String operadorLogistica)
    @POST
    @Path("/")
    public Response createEncomenda(EncomendaDTO encomendaDTO) throws MyConstraintViolationException, MyEntityNotFoundException, MyEntityExistsException {
        try {
            Encomenda encomenda = encomendaBean.create(encomendaDTO);
            return Response.status(Response.Status.CREATED).entity(toDTO(encomenda)).build();
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteEncomenda(@PathParam("id") long id) throws MyEntityNotFoundException {
        encomendaBean.remove(id);
        return Response.status(Response.Status.OK).build();
    }

    @PUT
    @Path("{id}/estado")
    public Response updateEncomendaEstado(@PathParam("id") long id, EncomendaDTO encomendaDTO) throws MyEntityNotFoundException {
        encomendaBean.updateEstado(id, encomendaDTO.getEstado());
        Encomenda encomenda = encomendaBean.getEncomendaById(id);
        return Response.status(Response.Status.OK).entity(toDTO(encomenda)).build();
    }
}
