package pt.ipleiria.estg.dei.ei.dae.backend.dtos;


public class SensorDTO {

    private long id;
    private String value;

    public SensorDTO() {
    }

    public SensorDTO(long id, String value) {
        this.id = id;
        this.value = value;
    }

    public SensorDTO(String value) {
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
