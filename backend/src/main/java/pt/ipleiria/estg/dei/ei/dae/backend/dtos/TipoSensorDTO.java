package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

public class TipoSensorDTO {
    private long id;
    private long idSensor;
    private String tipo;
    private String unidade;

    public TipoSensorDTO() {
    }

    public TipoSensorDTO(long id, long idSensor, String tipo, String unidade) {
        this.id = id;
        this.idSensor = idSensor;
        this.tipo = tipo;
        this.unidade = unidade;
    }

    public long getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(long idSensor) {
        this.idSensor = idSensor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
