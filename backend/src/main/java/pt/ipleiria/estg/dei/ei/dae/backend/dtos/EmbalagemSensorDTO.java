package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

public class EmbalagemSensorDTO {

    private long id;
    private long idEmbalagem;
    private long idSensor;
    private String dataInstalacao;

    public EmbalagemSensorDTO() {
    }

    public EmbalagemSensorDTO(long id, long idEmbalagem, long idSensor, String dataInstalacao) {
        this.id = id;
        this.idEmbalagem = idEmbalagem;
        this.idSensor = idSensor;
        this.dataInstalacao = dataInstalacao;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdEmbalagem() {
        return idEmbalagem;
    }

    public void setIdEmbalagem(long idEmbalagem) {
        this.idEmbalagem = idEmbalagem;
    }

    public long getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(long idSensor) {
        this.idSensor = idSensor;
    }

    public String getDataInstalacao() {
        return dataInstalacao;
    }

    public void setDataInstalacao(String dataInstalacao) {
        this.dataInstalacao = dataInstalacao;
    }
}
