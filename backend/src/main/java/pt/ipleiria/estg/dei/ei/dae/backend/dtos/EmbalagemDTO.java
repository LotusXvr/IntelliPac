package pt.ipleiria.estg.dei.ei.dae.backend.dtos;


public class EmbalagemDTO {
    private long id;
    private String material;

    public EmbalagemDTO() {
    }

    public EmbalagemDTO(long id, String material) {
        this.id = id;
        this.material = material;
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


}
