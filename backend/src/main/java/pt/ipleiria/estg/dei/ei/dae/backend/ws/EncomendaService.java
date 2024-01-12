package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EncomendaDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoFisicoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.EncomendaBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.ProdutoFisico;
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
                encomenda.getOperadorLogistica().getUsername(),
                encomenda.getEstado()
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
                produto.getFabricante().getUsername(),
                produto.getProdutoCatalogo().getId(),  // Adicione esta linha para obter o produtoCatalogoId
                produto.getEncomenda().getId()        // Adicione esta linha para obter o encomendaId
        );
    }


    // getAllEncomendas
    @GET
    @Path("/")
    public List<EncomendaDTO> getAllEncomendas() {
        return toDTOs(encomendaBean.getAllEncomendas());
    }

    // POST
    // createEncomenda (cliente)
    // Encomenda create(String consumidorFinal, String operadorLogistica)
    @POST
    @Path("/")
    public EncomendaDTO createEncomenda(EncomendaDTO encomendaDTO) throws MyConstraintViolationException, MyEntityNotFoundException, MyEntityExistsException {
        try {
            return toDTO(encomendaBean.create(encomendaDTO));
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    @DELETE
    @Path("{id}")
    public void deleteEncomenda(@PathParam("id") long id) throws MyEntityNotFoundException {
        encomendaBean.remove(id);
    }

    @PUT
    @Path("{id}")
    public void updateEncomenda(@PathParam("id") long id, EncomendaDTO encomendaDTO) throws MyEntityNotFoundException {
        encomendaBean.update(id, encomendaDTO.getEstado());
    }
}
