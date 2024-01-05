package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.mail.MessagingException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ClienteDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoCatalogoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoFisicoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.ProdutoCatalogoBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.ProdutoCatalogo;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.ProdutoFisico;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Path("produtosCatalogo")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ProdutoCatalogoService {

    @EJB
    private ProdutoCatalogoBean produtoCatalogoBean;

    @Context
    private SecurityContext securityContext;

    private ProdutoCatalogoDTO toDTONoProdutos(ProdutoCatalogo produtoCatalogo) {
        return new ProdutoCatalogoDTO(
                produtoCatalogo.getId(),
                produtoCatalogo.getNomeProduto(),
                produtoCatalogo.getFabricante().getUsername()
        );
    }

    private List<ProdutoCatalogoDTO> toDTOsNoProdutos(List<ProdutoCatalogo> produtosCatalogo) {
        return produtosCatalogo.stream().map(this::toDTONoProdutos).collect(Collectors.toList());
    }

    private ProdutoCatalogoDTO toDTO(ProdutoCatalogo produtoCatalogo) {
        return new ProdutoCatalogoDTO(
                produtoCatalogo.getId(),
                produtoCatalogo.getNomeProduto(),
                produtoCatalogo.getFabricante().getUsername(),
                produtosFisicosToDTOs(produtoCatalogo.getProdutos())
        );
    }

    private List<ProdutoCatalogoDTO> toDTOs(List<ProdutoCatalogo> produtosCatalogo) {
        return produtosCatalogo.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public List<ProdutoCatalogoDTO> getAllProdutosCatalogo() {
        return toDTOsNoProdutos(produtoCatalogoBean.getAllProductsCatalogo());
    }

    @POST
    @Path("/")
    public Response create(ProdutoCatalogoDTO produtoCatalogoDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        ProdutoCatalogo produtoCatalogo = produtoCatalogoBean.create(
                produtoCatalogoDTO.getNome(),
                produtoCatalogoDTO.getFabricanteUsername()
        );
        return Response.status(Response.Status.CREATED).entity(toDTONoProdutos(produtoCatalogo)).build();
    }

    @GET
    @Path("{id}")
    public Response getProdutoCatalogoDetails(@PathParam("id") long id) {

        ProdutoCatalogo produtoCatalogo = produtoCatalogoBean.getProdutoCatalogoWithProdutos(id);

        if (produtoCatalogo != null) {
            return Response.ok(toDTO(produtoCatalogo)).build();
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PRODUCT")
                .build();
    }

    public ProdutoFisicoDTO toDTONoProdutos(ProdutoFisico produtoFisico) {
        return new ProdutoFisicoDTO(
                produtoFisico.getId(),
                produtoFisico.getNomeProduto(),
                produtoFisico.getFabricante().getUsername(),
                produtoFisico.getProdutoCatalogo().getId()
        );
    }

    public List<ProdutoFisicoDTO> produtosFisicosToDTOs(List<ProdutoFisico> produtoFisico) {
        return produtoFisico.stream().map(this::toDTONoProdutos).collect(Collectors.toList());
    }

    @GET
    @Path("{id}/produtos")
    public Response getProdutoCatalogoProdutosFisicos(@PathParam("id") long id) {
        var produto = produtoCatalogoBean.getProdutoCatalogoWithProdutos(id);
        if (produto != null) {
            var dtos = produtosFisicosToDTOs(produto.getProdutos());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PRODUTOCATALOGO")
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteProdutoCatalogo(@PathParam("id") long id) throws MyEntityNotFoundException {
        produtoCatalogoBean.remove(id);
        return Response.ok().build();
    }

    @PUT
    @Path("{id}")
    public Response updateProdutoCatalogo(@PathParam("id") long id, ProdutoCatalogoDTO produtoCatalogoDTO) throws MyEntityNotFoundException {
        var produto = produtoCatalogoBean.find(id);
        if (produto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        produtoCatalogoBean.update(id, produtoCatalogoDTO.getNome(), produtoCatalogoDTO.getFabricanteUsername());
        return Response.ok().build();
    }
}
