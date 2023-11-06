package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

public class ProdutoDTO {
    private Long id;
    private String nome;
    private Long idFabricante; // Referência ao Fabricante de Produtos

    private String nomeFabricante;

    public ProdutoDTO() {
    }

    public ProdutoDTO(Long id, String nome, Long idFabricante) {
        this.id = id;
        this.nome = nome;
        this.idFabricante = idFabricante;
    }

    public ProdutoDTO(Long id, String nome, Long idFabricante, String nomeFabricante) {
        this.id = id;
        this.nome = nome;
        this.idFabricante = idFabricante;
        this.nomeFabricante = nomeFabricante;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getIdFabricante() {
        return idFabricante;
    }

    public void setIdFabricante(Long idFabricante) {
        this.idFabricante = idFabricante;
    }

    public String getNomeFabricante() {
        return nomeFabricante;
    }

    public void setNomeFabricante(String nomeFabricante) {
        this.nomeFabricante = nomeFabricante;
    }
}

