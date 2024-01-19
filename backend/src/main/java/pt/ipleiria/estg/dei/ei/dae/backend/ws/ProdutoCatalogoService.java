package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoCatalogoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoFisicoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.TipoEmbalagemDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.ClienteBean;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.FabricanteDeProdutosBean;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.ProdutoCatalogoBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.*;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backend.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("produtosCatalogo")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
public class ProdutoCatalogoService {

    @EJB
    private ProdutoCatalogoBean produtoCatalogoBean;

    @EJB
    private FabricanteDeProdutosBean fabricanteDeProdutosBean;

    @EJB
    private ClienteBean clienteBean;
    @Context
    private SecurityContext securityContext;

    private ProdutoCatalogoDTO toDTONoProdutos(ProdutoCatalogo produtoCatalogo) {
        return new ProdutoCatalogoDTO(
                produtoCatalogo.getId(),
                produtoCatalogo.getNomeProduto(),
                produtoCatalogo.getFabricante().getUsername(),
                produtoCatalogo.getPeso()
        );
    }

    private List<ProdutoCatalogoDTO> toDTOsNoProdutos(List<ProdutoCatalogo> produtosCatalogo) {
        return produtosCatalogo.stream().map(this::toDTONoProdutos).collect(Collectors.toList());
    }

    private ProdutoCatalogoDTO toDTO(ProdutoCatalogo produtoCatalogo) {
        ProdutoCatalogoDTO produtoCatalogoDTO = new ProdutoCatalogoDTO(
                produtoCatalogo.getId(),
                produtoCatalogo.getNomeProduto(),
                produtoCatalogo.getFabricante().getUsername(),
                produtoCatalogo.getPeso()
        );
        produtoCatalogoDTO.setProdutos(produtosFisicosToDTOs(produtoCatalogo.getProdutos()));
        produtoCatalogoDTO.setEmbalagensACriar(embalagenstoDTOs(produtoCatalogo.getEmbalagensACriar()));

        return produtoCatalogoDTO;
    }

    private TipoEmbalagemDTO toDTO(TipoEmbalagemProduto embalagensACriar) {
        return new TipoEmbalagemDTO(
                embalagensACriar.getId(),
                embalagensACriar.getTipoEmbalagem(),
                embalagensACriar.getMaterial(),
                embalagensACriar.getAltura(),
                embalagensACriar.getLargura(),
                embalagensACriar.getComprimento()
        );
    }

    private List<ProdutoCatalogoDTO> toDTOs(List<ProdutoCatalogo> produtosCatalogo) {
        return produtosCatalogo.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private List<TipoEmbalagemDTO> embalagenstoDTOs(List<TipoEmbalagemProduto> embalagensACriar) {
        return embalagensACriar.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    @RolesAllowed({"Cliente", "OperadorDeLogistica"})
    public List<ProdutoCatalogoDTO> getAllProdutosCatalogo() {
        return toDTOs(produtoCatalogoBean.getAllProductsCatalogo());
    }
    @RolesAllowed({"FabricanteDeProdutos"})
    @POST
    @Path("/")
    public Response create(ProdutoCatalogoDTO produtoCatalogoDTO) throws Exception {
        var principal = securityContext.getUserPrincipal();
        if(!principal.getName().equals(produtoCatalogoDTO.getFabricanteUsername())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        ProdutoCatalogo produtoCatalogo = produtoCatalogoBean.create(produtoCatalogoDTO);
        return Response.status(Response.Status.CREATED).entity(toDTONoProdutos(produtoCatalogo)).build();
    }
    @RolesAllowed({"FabricanteDeProdutos"})
    @PUT
    @Path("{id}")
    public Response updateProdutoCatalogo(@PathParam("id") long id, ProdutoCatalogoDTO produtoCatalogoDTO) throws MyEntityNotFoundException, MyConstraintViolationException {
        var produtoCatalogo = produtoCatalogoBean.find(id);
        var principal = securityContext.getUserPrincipal();
        if(!principal.getName().equals(produtoCatalogoDTO.getFabricanteUsername())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        if (produtoCatalogo != null) {
            produtoCatalogoBean.update(id, produtoCatalogoDTO);
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PRODUTOCATALOGO")
                .build();
    }

    @RolesAllowed({"FabricanteDeProdutos"})
    @GET
    @Path("{id}")
    public Response getProdutoCatalogoDetails(@PathParam("id") long id) {
        ProdutoCatalogo produtoCatalogo = produtoCatalogoBean.getProdutoCatalogoWithProdutos(id);
        var principal = securityContext.getUserPrincipal();
        if(!principal.getName().equals(produtoCatalogo.getFabricante().getUsername())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

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
                produtoFisico.getProdutoCatalogo().getId(),
                produtoFisico.getEncomenda().getId(),
                produtoFisico.getPeso()
        );
    }

    public List<ProdutoFisicoDTO> produtosFisicosToDTOs(List<ProdutoFisico> produtoFisico) {
        return produtoFisico.stream().map(this::toDTONoProdutos).collect(Collectors.toList());
    }
    @RolesAllowed({"FabricanteDeProdutos"})
    @GET
    @Path("{id}/produtos")
    public Response getProdutoCatalogoProdutosFisicos(@PathParam("id") long id) {
        var produto = produtoCatalogoBean.getProdutoCatalogoWithProdutos(id);

        var principal = securityContext.getUserPrincipal();

        if(produto == null){
            return Response.status(Response.Status.NOT_FOUND).entity("ERROR_FINDING_PRODUTOCATALOGO").build();
        }
        if(!principal.getName().equals(produto.getFabricante().getUsername())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        var dtos = produtosFisicosToDTOs(produto.getProdutos());
        return Response.ok(dtos).build();
    }

    @RolesAllowed({"FabricanteDeProdutos"})
    @DELETE
    @Path("{id}")
    public Response deleteProdutoCatalogo(@PathParam("id") long id) throws MyEntityNotFoundException {
        var produto = produtoCatalogoBean.find(id);
        if(produto == null){
            return Response.status(Response.Status.NOT_FOUND).entity("ERROR_FINDING_PRODUTOCATALOGO").build();
        }

        var principal = securityContext.getUserPrincipal();
        if(!principal.getName().equals(produto.getFabricante().getUsername())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        produtoCatalogoBean.remove(id);
        return Response.ok().build();
    }
    @RolesAllowed({"FabricanteDeProdutos", "Cliente"})
    @GET
    @Path("fabricante/{username}")
    public List<ProdutoCatalogoDTO> getProdutosFromFabricante(@PathParam("username") String username) throws Exception {
        Cliente cliente = clienteBean.find(username);
        var principal = securityContext.getUserPrincipal();
        if(!principal.getName().equals(username)){
            throw new Exception("Não pode aceder aos produtos");
        }
        if(cliente != null){
            return toDTOs(produtoCatalogoBean.getAllProductsCatalogo());
        }
        FabricanteDeProdutos fabricanteDeProdutos = fabricanteDeProdutosBean.find(username);
        if(fabricanteDeProdutos != null){
            return toDTOs(produtoCatalogoBean.getProdutosCatalogoFromFabricante(username));
        }

        throw new MyEntityExistsException("Operador de logistica nao pode ver os produtos");
    }

}
