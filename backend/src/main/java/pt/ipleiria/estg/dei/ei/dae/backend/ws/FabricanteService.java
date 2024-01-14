package pt.ipleiria.estg.dei.ei.dae.backend.ws;

import jakarta.ejb.EJB;
import jakarta.mail.MessagingException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.EmailDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.FabricanteProdutoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.dtos.ProdutoDTO;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.backend.ejbs.FabricanteDeProdutosBean;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.FabricanteDeProdutos;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Produto;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Path("/fabricantes")
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class FabricanteService {

    @EJB
    private FabricanteDeProdutosBean fabricanteDeProdutosBean;

    @EJB
    private EmailBean emailBean;

    private FabricanteProdutoDTO toDTO(FabricanteDeProdutos fabricante) {
        FabricanteProdutoDTO fabricanteDeProdutosDTO = new FabricanteProdutoDTO(
                fabricante.getUsername(),
                fabricante.getPassword(),
                fabricante.getName(),
                fabricante.getEmail()
        );

        fabricanteDeProdutosDTO.setProdutos(produtosToDTOs(fabricante.getProdutos()));
        return fabricanteDeProdutosDTO;
    }

    private FabricanteProdutoDTO toDTONoProdutos(FabricanteDeProdutos fabricante) {
        return new FabricanteProdutoDTO(
                fabricante.getUsername(),
                fabricante.getPassword(),
                fabricante.getName(),
                fabricante.getEmail()
        );
    }

    private List<FabricanteProdutoDTO> toDTOsNoProdutos(List<FabricanteDeProdutos> fabricantes) {
        return fabricantes.stream().map(this::toDTONoProdutos).collect(java.util.stream.Collectors.toList());
    }

    private ProdutoDTO toDTOProducts(Produto produto) {
        return new ProdutoDTO(
                produto.getId(),
                produto.getNomeProduto(),
                produto.getFabricante().getUsername()
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
        return toDTOsNoProdutos(fabricanteDeProdutosBean.getAllFabricantes());
    }

    @POST
    @Path("/")
    public void createNewFabricante(FabricanteProdutoDTO fabricanteProdutoDTO) throws MyEntityExistsException {
        if (fabricanteDeProdutosBean.exists(fabricanteProdutoDTO.getNome())) {
            throw new MyEntityExistsException("FabricanteDeProdutos with nomeFabricante '" + fabricanteProdutoDTO.getNome() + "' already exists");
        }

        fabricanteDeProdutosBean.create(
                // String username, String password, String nome, String email
                fabricanteProdutoDTO.getUsername(),
                fabricanteProdutoDTO.getPassword(),
                fabricanteProdutoDTO.getNome(),
                fabricanteProdutoDTO.getEmail()
        );
    }

    @DELETE
    @Path("{username}")
    public void deleteFabricante(@PathParam("username") String username) throws Exception {
        fabricanteDeProdutosBean.remove(username);
    }

    @PUT
    @Path("{username}")
    public void updateFabricante(@PathParam("username") String username, FabricanteProdutoDTO fabricanteProdutoDTO) {
        FabricanteDeProdutos fabricanteDeProdutos = fabricanteDeProdutosBean.find(username);
        fabricanteDeProdutos.setName(fabricanteProdutoDTO.getNome());
        fabricanteDeProdutosBean.update(fabricanteDeProdutos);
    }

    @GET
    @Path("{username}")
    public FabricanteProdutoDTO getFabricanteDetails(@PathParam("username") String username) {
        FabricanteDeProdutos fabricante = fabricanteDeProdutosBean.find(username);
        return toDTO(fabricante);
    }

    @POST
    @Path("/{username}/email/send")
    public Response sendEmail(@PathParam("username") String username, EmailDTO email)
            throws MyEntityNotFoundException, MessagingException {
        FabricanteDeProdutos fabricanteDeProdutos = fabricanteDeProdutosBean.find(username);
        if (fabricanteDeProdutos == null) {
            throw new MyEntityNotFoundException("Student with id '" + username
                    + "' not found in our records.");
        }
        emailBean.send(fabricanteDeProdutos.getEmail(), email.getSubject(), email.getMessage());
        return Response.status(Response.Status.OK).entity("E-mail sent").build();
    }

}
