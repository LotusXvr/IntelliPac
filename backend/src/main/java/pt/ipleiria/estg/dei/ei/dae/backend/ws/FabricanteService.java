package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.FabricanteProdutoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.FabricanteDeProdutosBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.FabricanteDeProdutos;

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
}
