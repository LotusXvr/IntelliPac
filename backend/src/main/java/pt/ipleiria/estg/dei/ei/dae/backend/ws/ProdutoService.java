package pt.ipleiria.estg.dei.ei.dae.backend.ws;


import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.ProdutoBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Produto;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Path("/produtos")
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class ProdutoService {
    @EJB
    private ProdutoBean produtoBean;

    // Converts an entity Student to a DTO Student class
    private ProdutoDTO toDTO(Produto produto) {
        return new ProdutoDTO(
                produto.getId(),
                produto.getNomeProduto(),
                produto.getFabricante().getId(),
                produto.getFabricante().getNome()
        );
    }
    // converts an entire list of entities into a list of DTOs
    private List<ProdutoDTO> toDTOs(List<Produto> produtos) {
        return produtos.stream().map(this::toDTO).collect(Collectors.toList());
    }
    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/produtos/”
    public List<ProdutoDTO> getAllProducts() {
        return toDTOs(produtoBean.getAllProducts());
    }

    @POST
    @Path("/")
    public void createNewProduct(ProdutoDTO produtoDTO) throws MyEntityExistsException, MyEntityNotFoundException {
        produtoBean.createProduto(produtoDTO.getNome(), produtoDTO.getIdFabricante());
    }

    @PUT
    @Path("{id}")
    public void updateProduct(@PathParam("id") long id, ProdutoDTO produtoDTO) throws MyEntityNotFoundException {
        produtoBean.update(id, produtoDTO.getNome(), produtoDTO.getIdFabricante());
    }

    @DELETE
    @Path("{id}")
    public void deleteProduct(@PathParam("id") long id) throws MyEntityNotFoundException {
        produtoBean.remove(id);
    }

    @GET
    @Path("{id}")
    public ProdutoDTO getProductDetails(@PathParam("id") long id) throws MyEntityNotFoundException {
        return toDTO(produtoBean.findProduto(id));
    }


}
