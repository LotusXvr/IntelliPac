package pt.ipleiria.estg.dei.ei.dae.backend.dtos;


public class EmbalagemDTO {
    private Long id;
    private String material;
    private String valoresObservados;

    public EmbalagemDTO() {
    }

    public EmbalagemDTO(Long id, String material, String valoresObservados) {
        this.id = id;
        this.material = material;
        this.valoresObservados = valoresObservados;
    }

    // Getters e Setters para os atributos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getValoresObservados() {
        return valoresObservados;
    }

    public void setValoresObservados(String valoresObservados) {
        this.valoresObservados = valoresObservados;
    }


}
