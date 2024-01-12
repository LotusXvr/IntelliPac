package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EncomendaDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoFisicoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.EncomendaBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.ProdutoFisico;

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
                produtosToDTOs(encomenda.getProdutos())
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

    // toDTO
    private ProdutoFisicoDTO toDTO(ProdutoFisico produto) {
        return new ProdutoFisicoDTO(
                produto.getId(),
                produto.getNomeProduto(),
                produto.getFabricante().getUsername()
        );
    }

    // getAllEncomendas
    @GET
    @Path("/")
    public List<EncomendaDTO> getAllEncomendas() {
        return toDTOs(encomendaBean.getAllEncomendasCliente(securityContext.getUserPrincipal().getName()));
    }

    // getAllEncomendas (operador)
    @GET
    @Path("/operador/{username}")
    public List<EncomendaDTO> getAllEncomendasAdmin() {
        return toDTOs(encomendaBean.getAllEncomendasOperadoresLogistica("ValterLogo"));
    }
}
