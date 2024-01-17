package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import java.util.ArrayList;
import java.util.List;

public class EmbalagemDeProdutoDTO {
    private Long id;

    private String material;
    private long tipoEmbalagem;

    private long altura;
    private long largura;
    private long comprimento;
    private List<ProdutoFisicoDTO> produtos;

    private List<SensorDTO> sensores;

    public EmbalagemDeProdutoDTO() {
        this.produtos = new ArrayList<>();
        this.sensores = new ArrayList<>();
    }

    public EmbalagemDeProdutoDTO(Long id, String material, long tipoEmbalagem, long altura, long largura, long comprimento) {
        this.id = id;
        this.material = material;
        this.tipoEmbalagem = tipoEmbalagem;
        this.altura = altura;
        this.largura = largura;
        this.comprimento = comprimento;
        this.produtos = new ArrayList<>();
        this.sensores = new ArrayList<>();
    }

    public EmbalagemDeProdutoDTO(long id, String material, long tipoEmbalagem, List<SensorDTO> sensores, long altura, long largura, long comprimento) {
        this.id = id;
        this.material = material;
        this.tipoEmbalagem = tipoEmbalagem;
        this.sensores = sensores;
        this.altura = altura;
        this.largura = largura;
        this.comprimento = comprimento;
    }

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

    public long getTipoEmbalagem() {
        return tipoEmbalagem;
    }

    public void setTipoEmbalagem(long tipoEmbalagem) {
        this.tipoEmbalagem = tipoEmbalagem;
    }

    public List<ProdutoFisicoDTO> getProdutos() {
        return produtos;
    }

    public void setProdutoDTOS(List<ProdutoFisicoDTO> produtoDTOS) {
        this.produtos = produtoDTOS;
    }

    public List<SensorDTO> getSensores() {
        return sensores;
    }

    public void setSensorDTOS(List<SensorDTO> sensorDTOS) {
        this.sensores = sensorDTOS;
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
