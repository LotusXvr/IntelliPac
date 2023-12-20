package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.FabricanteProdutoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.FabricanteDeProdutosBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.FabricanteDeProdutos;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Produto;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;

import java.util.List;
import java.util.stream.Collectors;

@Path("/fabricantes")
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class FabricanteService {

    @EJB
    private FabricanteDeProdutosBean fabricanteDeProdutosBean;

    private FabricanteProdutoDTO toDTO(FabricanteDeProdutos fabricante) {
        return new FabricanteProdutoDTO(
                fabricante.getId(),
                fabricante.getNome(),
                produtosToDTOs(fabricante.getProdutos())
        );
    }

private ProdutoDTO toDTOProducts(Produto produto) {
        return new ProdutoDTO(
                produto.getId(),
                produto.getNomeProduto()
        );
    }

    private List<ProdutoDTO> produtosToDTOs(List<Produto> produtos) {
        return produtos.stream().map(this::toDTOProducts).collect(Collectors.toList());
    }


    private List<FabricanteProdutoDTO> toDTOs(List<FabricanteDeProdutos> fabricantes) {
        return fabricantes.stream().map(this::toDTO).collect(java.util.stream.Collectors.toList());
    }

    @GET
    @Path("/")
    public List<FabricanteProdutoDTO> getAllFabricantes() {
        return toDTOs(fabricanteDeProdutosBean.getAllFabricantes());
    }

    @POST
    @Path("/")
    public void createNewFabricante(FabricanteProdutoDTO fabricanteProdutoDTO) throws MyEntityExistsException {
        if (fabricanteDeProdutosBean.exists(fabricanteProdutoDTO.getNome())) {
            throw new MyEntityExistsException("FabricanteDeProdutos with nomeFabricante '" + fabricanteProdutoDTO.getNome() + "' already exists");
        }

        fabricanteDeProdutosBean.create(
                fabricanteProdutoDTO.getNome()
        );
    }

    @DELETE
    @Path("{id}")
    public void deleteFabricante(@PathParam("id") long id) {
        fabricanteDeProdutosBean.remove(
                fabricanteDeProdutosBean.find(id)
        );
    }

    @PUT
    @Path("{id}")
    public void updateFabricante(@PathParam("id") long id, FabricanteProdutoDTO fabricanteProdutoDTO) {
        FabricanteDeProdutos fabricanteDeProdutos = fabricanteDeProdutosBean.find(id);
        fabricanteDeProdutos.setNome(fabricanteProdutoDTO.getNome());
        fabricanteDeProdutosBean.update(fabricanteDeProdutos);
    }

    @GET
    @Path("{id}")
    public FabricanteProdutoDTO getFabricanteDetails(@PathParam("id") long id) {
        FabricanteDeProdutos fabricante = fabricanteDeProdutosBean.find(id);
        return toDTO(fabricante);
    }

}
