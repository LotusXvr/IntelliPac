package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.FabricanteProdutoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.FabricanteDeProdutosBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.FabricanteDeProdutos;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;

import java.util.List;

@Path("/fabricantes")
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class FabricanteService {

    @EJB
    private FabricanteDeProdutosBean fabricanteDeProdutosBean;

    private FabricanteProdutoDTO toDTO(FabricanteDeProdutos fabricante) {
        return new FabricanteProdutoDTO(
                fabricante.getId(),
                fabricante.getNomeFabricante()
        );
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
        if (fabricanteDeProdutosBean.exists(fabricanteProdutoDTO.getNomeFabricante())) {
            throw new MyEntityExistsException("FabricanteDeProdutos with nomeFabricante '" + fabricanteProdutoDTO.getNomeFabricante() + "' already exists");
        }

        fabricanteDeProdutosBean.createFabricanteDeProdutos(
                fabricanteProdutoDTO.getNomeFabricante()
        );
    }

    @DELETE
    @Path("{id}")
    public void deleteFabricante(@PathParam("id") long id) {
        fabricanteDeProdutosBean.removeFabricanteDeProdutos(
                fabricanteDeProdutosBean.findFabricanteDeProdutosById(id)
        );
    }

    @PUT
    @Path("{id}")
    public void updateFabricante(@PathParam("id") long id, FabricanteProdutoDTO fabricanteProdutoDTO) {
        FabricanteDeProdutos fabricanteDeProdutos = fabricanteDeProdutosBean.findFabricanteDeProdutosById(id);
        fabricanteDeProdutos.setNomeFabricante(fabricanteProdutoDTO.getNomeFabricante());
        fabricanteDeProdutosBean.updateFabricanteDeProdutos(fabricanteDeProdutos);
    }

    @GET
    @Path("{id}")
    public FabricanteProdutoDTO getFabricanteDetails(@PathParam("id") long id) {
        return toDTO(fabricanteDeProdutosBean.findFabricanteDeProdutosById(id));
    }


}
