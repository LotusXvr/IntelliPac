package pt.ipleiria.estg.dei.ei.dae.backend.dtos;


public class EmbalagemDTO {
    private Long id;
    private String tipo;
    private String material;
    private Long idProduto; // Referência ao Produto
    private String valoresObservados;

    public EmbalagemDTO() {
    }

    public EmbalagemDTO(Long id, String tipo, String material, Long idProduto, String valoresObservados) {
        this.id = id;
        this.tipo = tipo;
        this.material = material;
        this.idProduto = idProduto;
        this.valoresObservados = valoresObservados;
    }

    // Getters e Setters para os atributos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public String getValoresObservados() {
        return valoresObservados;
    }

    public void setValoresObservados(String valoresObservados) {
        this.valoresObservados = valoresObservados;
    }
}
