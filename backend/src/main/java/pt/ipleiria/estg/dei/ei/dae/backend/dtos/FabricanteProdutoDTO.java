package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import pt.ipleiria.estg.dei.ei.dae.backend.entities.Produto;

import java.io.Serializable;
import java.util.List;

public class FabricanteProdutoDTO implements Serializable {
    private long id;
    private String nome;

    private List<ProdutoDTO> produtos;

    public FabricanteProdutoDTO() {

    }

    public FabricanteProdutoDTO(long id, String nome, List<ProdutoDTO> produtos) {
        this.id = id;
        this.nome = nome;
        this.produtos = produtos;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<ProdutoDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoDTO> produtos) {
        this.produtos = produtos;
    }
}
