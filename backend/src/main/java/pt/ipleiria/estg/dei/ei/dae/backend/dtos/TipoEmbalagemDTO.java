package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

public class TipoEmbalagemDTO {

    private long id;
    private long tipo;
    private String material;

    private long altura;
    private long largura;
    private long comprimento;

    public TipoEmbalagemDTO() {
    }

    public TipoEmbalagemDTO(long id, long tipo, String material, long altura, long largura, long comprimento) {
        this.id = id;
        this.tipo = tipo;
        this.material = material;
        this.altura = altura;
        this.largura = largura;
        this.comprimento = comprimento;
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

    public long getAltura() {
        return altura;
    }

    public void setAltura(long altura) {
        this.altura = altura;
    }

    public long getLargura() {
        return largura;
    }

    public void setLargura(long largura) {
        this.largura = largura;
    }

    public long getComprimento() {
        return comprimento;
    }

    public void setComprimento(long comprimento) {
        this.comprimento = comprimento;
    }
}

