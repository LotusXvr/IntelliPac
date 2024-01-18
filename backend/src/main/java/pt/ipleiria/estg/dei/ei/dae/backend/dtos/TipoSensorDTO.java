package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import pt.ipleiria.estg.dei.ei.dae.backend.entities.TipoEmbalagemProduto;

import java.util.ArrayList;
import java.util.List;

public class TipoSensorDTO {
    private long id;
    private String tipo;
    private String unidade;
    private long estado;
    private List<TipoEmbalagemDTO> tipoEmbalagemDTOS;

    public TipoSensorDTO() {
    }

    public TipoSensorDTO(long id, String tipo, String unidade,long estado) {
        this.estado = estado;
        this.id = id;
        this.tipo = tipo;
        this.unidade = unidade;
        this.tipoEmbalagemDTOS = new ArrayList<>();
    }

    public long getEstado() {
        return estado;
    }

    public void setEstado(long estado) {
        this.estado = estado;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<TipoEmbalagemDTO> getTipoEmbalagemDTOS() {
        return tipoEmbalagemDTOS;
    }

    public void setTipoEmbalagemDTOS(List<TipoEmbalagemDTO> tipoEmbalagemDTOS) {
        this.tipoEmbalagemDTOS = tipoEmbalagemDTOS;
    }
}
