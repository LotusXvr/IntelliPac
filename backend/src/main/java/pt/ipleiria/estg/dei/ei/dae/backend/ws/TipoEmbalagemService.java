package pt.ipleiria.estg.dei.ei.dae.backend.ws;


import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.TipoEmbalagemDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.TipoEmbalagemProdutoBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.TipoEmbalagemProduto;

import java.util.List;

@Path("tipoEmbalagens") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class TipoEmbalagemService {

    @EJB
    private TipoEmbalagemProdutoBean tipoEmbalagemBean;

    private TipoEmbalagemDTO toDTO(TipoEmbalagemProduto tipoEmbalagem) {
        return new TipoEmbalagemDTO(
                tipoEmbalagem.getId(),
                tipoEmbalagem.getTipoEmbalagem(),
                tipoEmbalagem.getMaterial(),
                tipoEmbalagem.getAltura(),
                tipoEmbalagem.getLargura(),
                tipoEmbalagem.getComprimento()
        );
    }

    private List<TipoEmbalagemDTO> toDTOs(List<TipoEmbalagemProduto> tipoEmbalagens) {
        return tipoEmbalagens.stream().map(this::toDTO).collect(java.util.stream.Collectors.toList());
    }

    @GET
    @Path("/")
    public List<TipoEmbalagemDTO> getAllTipoEmbalagem() {
        return toDTOs(tipoEmbalagemBean.getAllTipoEmbalagem());
    }

}
