package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoFisicoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.ProdutoFisicoBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.ProdutoFisico;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;

public class ProdutoFisicoService {
    @EJB
    private ProdutoFisicoBean produtoFisicoBean;

    private ProdutoFisicoDTO toDTO(ProdutoFisico produtoFisico) {
        return new ProdutoFisicoDTO(
                produtoFisico.getId(),
                produtoFisico.getNomeProduto(),
                produtoFisico.getFabricante().getUsername(),
                produtoFisico.getProdutoCatalogo().getId()
        );
    }

    private List<ProdutoFisicoDTO> toDTOs(List<ProdutoFisico> produtosFisicos) {
        return produtosFisicos.stream().map(this::toDTO).collect(java.util.stream.Collectors.toList());
    }

    @GET
    @Path("/")
    public List<ProdutoFisicoDTO> getAllProdutos() {
        return toDTOs(produtoFisicoBean.getAllProductsFisico());
    }

    @GET
    @Path("{id}")
    public ProdutoFisicoDTO getProdutoFisicoDetails(@PathParam("id") long id) {
        ProdutoFisico produtoFisico = produtoFisicoBean.find(id);
        return toDTO(produtoFisico);
    }

    @POST
    @Path("/")
    public Response createNewProdutoFisico (ProdutoFisicoDTO produtoFisicoDTO) throws MyConstraintViolationException, MyEntityNotFoundException, MyEntityExistsException {
        produtoFisicoBean.create(
                produtoFisicoDTO.getNome(),
                produtoFisicoDTO.getFabricanteUsername(),
                produtoFisicoDTO.getProdutoCatalogoId()
        );

        ProdutoFisico newProdutoFisico = produtoFisicoBean.find(produtoFisicoDTO.getId());
        if(newProdutoFisico == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        return  Response.status(Response.Status.CREATED).entity(toDTO(newProdutoFisico)).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteProdutoFisico(@PathParam("id") long id) {
        try {
            produtoFisicoBean.remove(id);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ERROR_DELETING_PRODUTOFISICO").build();
        }
    }

    @PUT
    @Path("{id}")
    public Response updateProdutoFisico(@PathParam("id") long id, ProdutoFisicoDTO produtoFisicoDTO) {
        try {
            produtoFisicoBean.update(id, produtoFisicoDTO.getNome(), produtoFisicoDTO.getFabricanteUsername(), produtoFisicoDTO.getProdutoCatalogoId());
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ERROR_UPDATING_PRODUTOFISICO").build();
        }
    }

}
