package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import java.util.ArrayList;
import java.util.List;

public class FabricanteProdutoDTO{
    private Long id;
    private String nomeFabricante;
    private List<ProdutoDTO> produtos;

    public FabricanteProdutoDTO() {
        this.produtos = new ArrayList<>();
    }

    public FabricanteProdutoDTO(Long id, String nomeFabricante, List<ProdutoDTO> produtos) {
        this.id = id;
        this.nomeFabricante = nomeFabricante;
        this.produtos = produtos;
    }

    public FabricanteProdutoDTO(Long id, String nomeFabricante) {
        this.id = id;
        this.nomeFabricante = nomeFabricante;
        this.produtos = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeFabricante() {
        return nomeFabricante;
    }

    public void setNomeFabricante(String nomeFabricante) {
        this.nomeFabricante = nomeFabricante;
    }

    public List<ProdutoDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoDTO> produtos) {
        this.produtos = produtos;
    }
}
