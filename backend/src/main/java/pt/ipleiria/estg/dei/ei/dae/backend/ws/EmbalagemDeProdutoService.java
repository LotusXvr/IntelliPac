package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EmbalagemDeProdutoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoCatalogoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.EmbalagemDeProdutoBean;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.ProdutoCatalogoBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.EmbalagemDeProduto;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.ProdutoCatalogo;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Path("embalagensDeProduto")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class EmbalagemDeProdutoService {

    @EJB
    private EmbalagemDeProdutoBean embalagemDeProdutoBean;
    private EmbalagemDeProdutoDTO toDTO(EmbalagemDeProduto embalagemDeProduto) {
        return new EmbalagemDeProdutoDTO(
                embalagemDeProduto.getId(),
                embalagemDeProduto.getMaterial(),
                embalagemDeProduto.getTipoEmbalagem()
        );
    }

    private List<EmbalagemDeProdutoDTO> toDTOs(List<EmbalagemDeProduto> embalagemDeProdutos) {
        return embalagemDeProdutos.stream().map(this::toDTO).collect(Collectors.toList());
    }
    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public List<EmbalagemDeProdutoDTO> getAll() {
        return toDTOs(embalagemDeProdutoBean.getAllEmbalagensDeProduto());
    }

    @POST
    @Path("/") // means: the relative url path is “/api/students/”
    public Response create(EmbalagemDeProdutoDTO embalagemDeProdutoDTO)
        throws Exception {
            EmbalagemDeProduto embalagemDeProduto = embalagemDeProdutoBean.create(
                    embalagemDeProdutoDTO.getMaterial(),
                    embalagemDeProdutoDTO.getTipoEmbalagem()
            );
            return Response.status(Response.Status.CREATED).entity(toDTO(embalagemDeProduto)).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") long id, EmbalagemDeProdutoDTO embalagemDeProdutoDTO) throws MyEntityNotFoundException {
        var embalagemDeProduto = embalagemDeProdutoBean.find(id);
        if (embalagemDeProduto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        embalagemDeProdutoBean.update(id, embalagemDeProdutoDTO.getMaterial(), embalagemDeProduto.getTipoEmbalagem());
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteProdutoCatalogo(@PathParam("id") long id) throws MyEntityNotFoundException {
        embalagemDeProdutoBean.remove(id);
        return Response.ok().build();
    }
}
