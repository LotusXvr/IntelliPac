package pt.ipleiria.estg.dei.ei.dae.backend.ws;

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

import java.util.List;
import java.util.stream.Collectors;

@Path("produtosCatalogo")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
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
    public List<ProdutoCatalogoDTO> getAllProdutosCatalogo() {
        return toDTOs(produtoCatalogoBean.getAllProductsCatalogo());
    }

    @POST
    @Path("/")
    public Response create(ProdutoCatalogoDTO produtoCatalogoDTO)
            throws Exception {
        ProdutoCatalogo produtoCatalogo = produtoCatalogoBean.create(produtoCatalogoDTO);
        return Response.status(Response.Status.CREATED).entity(toDTONoProdutos(produtoCatalogo)).build();
    }

    @PUT
    @Path("{id}")
    public Response updateProdutoCatalogo(@PathParam("id") long id, ProdutoCatalogoDTO produtoCatalogoDTO) throws MyEntityNotFoundException, MyConstraintViolationException {
        var produtoCatalogo = produtoCatalogoBean.find(id);
        if (produtoCatalogo != null) {
            produtoCatalogoBean.update(id, produtoCatalogoDTO);
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PRODUTOCATALOGO")
                .build();
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
                produtoFisico.getProdutoCatalogo().getId(),
                produtoFisico.getEncomenda().getId(),
                produtoFisico.getPeso()
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

    @GET
    @Path("fabricante/{username}")
    public List<ProdutoCatalogoDTO> getProdutosFromFabricante(@PathParam("username") String username) throws MyEntityExistsException {
        Cliente cliente = clienteBean.find(username);
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
