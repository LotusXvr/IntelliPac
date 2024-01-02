package pt.ipleiria.estg.dei.ei.dae.backend.dtos;


import pt.ipleiria.estg.dei.ei.dae.backend.entities.Embalagem;

import java.util.ArrayList;
import java.util.List;

public class SensorDTO {

    private long id;
    private long idSensor;
    private String valor;
    private String tipo;
    private String unidade;
    private List<Embalagem> embalagens;

    public SensorDTO() {
        this.embalagens = new ArrayList<>();
    }

    public SensorDTO(long idSensor, String valor, String tipo, String unidade) {
        this.idSensor = idSensor;
        this.valor = valor;
        this.tipo = tipo;
        this.unidade = unidade;
        this.embalagens = new ArrayList<>();
    }

    public List<Embalagem> getEmbalagens() {
        return embalagens;
    }

    public void setEmbalagens(List<Embalagem> embalagens) {
        this.embalagens = embalagens;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

        public long getIdSensor() {
            return idSensor;
        }

        public void setIdSensor(long idSensor) {
            this.idSensor = idSensor;
        }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
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
}
