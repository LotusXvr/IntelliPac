package pt.ipleiria.estg.dei.ei.dae.backend.dtos;


import java.util.ArrayList;
import java.util.List;

public class EmbalagemDTO {
    private long id;
    private String material;

    private List<SensorDTO> sensores;

    private long altura;

    private long largura;

    private long comprimento;

    public EmbalagemDTO() {
        this.sensores = new ArrayList<>();
    }

    public EmbalagemDTO(long id, String material, long altura, long largura, long comprimento) {
        this.id = id;
        this.material = material;
        this.altura = altura;
        this.largura = largura;
        this.comprimento = comprimento;
        this.sensores = new ArrayList<>();
    }

    public EmbalagemDTO(long id, String material, List<SensorDTO> sensores) {
        this.id = id;
        this.material = material;
        this.sensores = sensores;
    }

    public List<SensorDTO> getSensores() {
        return sensores;
    }

    public void setSensores(List<SensorDTO> sensores) {
        this.sensores = sensores;
    }

    public void addSensor(SensorDTO sensor) {
        this.sensores.add(sensor);
    }

    public void removeSensor(SensorDTO sensor) {
        this.sensores.remove(sensor);
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
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
