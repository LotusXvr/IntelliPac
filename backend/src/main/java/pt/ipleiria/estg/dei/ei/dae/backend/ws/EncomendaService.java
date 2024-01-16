package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.mail.MessagingException;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EmbalagemDeProdutoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EmbalagemDeTransporteDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EncomendaDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoFisicoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.ClienteBean;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.EncomendaBean;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.OperadorDeLogisticaBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.*;
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

    @EJB
    private ClienteBean clienteBean;

    @EJB
    private OperadorDeLogisticaBean opBean;

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


    private List<EmbalagemDeTransporteDTO> embalagensTransporteToDTOs(List<EmbalagemDeTransporte> embalagemTransportes) {
        return embalagemTransportes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private EmbalagemDeTransporteDTO toDTO(EmbalagemDeTransporte embalagemDeTransporte) {
        return new EmbalagemDeTransporteDTO(
                embalagemDeTransporte.getId(),
                embalagemDeTransporte.getMaterial()
                //toDTOs(embalagemDeTransporte.getEncomendas())
        );
    }

    // toDTO
    private ProdutoFisicoDTO toDTO(ProdutoFisico produto) {
        return new ProdutoFisicoDTO(
                produto.getId(),
                produto.getNomeProduto(),
                produto.getFabricante().getUsername(),
                produto.getProdutoCatalogo().getId(),
                produto.getEncomenda().getId(),
                embalagensDeProdutoToDTOs(produto.getEmbalagensDeProduto())
        );
    }

    private List<ProdutoFisicoDTO> produtosToDTOs(List<ProdutoFisico> produtos) {
        return produtos.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private EmbalagemDeProdutoDTO toDTO(EmbalagemDeProduto embalagemDeProduto) {
        return new EmbalagemDeProdutoDTO(
                embalagemDeProduto.getId(),
                embalagemDeProduto.getMaterial(),
                embalagemDeProduto.getTipoEmbalagem()
                //toDTOs(embalagemDeTransporte.getEncomendas())
        );
    }

    private List<EmbalagemDeProdutoDTO> embalagensDeProdutoToDTOs(List<EmbalagemDeProduto> embalagensProduto) {
        return embalagensProduto.stream().map(this::toDTO).collect(Collectors.toList());
    }


    // getAllEncomendas
    @GET
    @Path("/")
    public List<EncomendaDTO> getAllEncomendas() {
        return toDTOs(encomendaBean.getAllEncomendas());
    }

    // getEncomendasByCliente
    @GET
    @Path("/username/{username}")
    public List<EncomendaDTO> getEncomendasByUsername(@PathParam("username") String username) throws MyEntityNotFoundException {
        Cliente cliente = clienteBean.find(username);
        if (cliente != null) {
            return toDTOs(encomendaBean.getAllEncomendasCliente(username));
        }

        OperadorDeLogistica operadorDeLogistica = opBean.find(username);
        if (operadorDeLogistica != null) {
            return toDTOs(encomendaBean.getAllEncomendasOperadoresLogistica(username));
        }

        throw new MyEntityNotFoundException("Cliente ou Operador de Logistica com username " + username + " não existe");
    }

    // getEncomendaById
    @GET
    @Path("/{id}")
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

    @PATCH
    @Path("{id}/estado")
    public Response patchEncomendaEstado(@PathParam("id") long id, EncomendaDTO encomendaDTO) throws MyEntityNotFoundException, MessagingException {
        encomendaBean.patchEstado(id, encomendaDTO.getEstado());
        return Response.status(Response.Status.OK).entity("Estado alterado para " + encomendaDTO.getEstado() + " com sucesso").build();
    }

    @PATCH
    @Path("{id}/embalagensDeTransporte")
    public Response patchEncomendaEmbalagensTransporte(@PathParam("id") long id, EncomendaDTO encomendaDTO) throws MyEntityNotFoundException {
        System.out.println("565656 " + encomendaDTO.getEmbalagensTransporte());
        encomendaBean.patchEmbalagensTransporte(id, encomendaDTO.getEmbalagensTransporte());
        return Response.status(Response.Status.OK).entity("Embalagens de Transporte alteradas com sucesso").build();
    }
}
