package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

public class TipoEmbalagemDTO {

    private long id;
    private long tipo;
    private String material;

    public TipoEmbalagemDTO() {
    }

    public TipoEmbalagemDTO(long id, long tipo, String material) {
        this.id = id;
        this.tipo = tipo;
        this.material = material;
    }

    public long getTipo() {
        return tipo;
    }

    public void setTipo(long tipo) {
        this.tipo = tipo;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

