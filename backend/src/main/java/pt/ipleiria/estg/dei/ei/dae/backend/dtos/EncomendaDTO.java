package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import java.util.ArrayList;
import java.util.List;

public class EncomendaDTO {
    private Long id;
    private String consumidorFinal;
    private String dataEncomenda;
    private List<ProdutoFisicoDTO> produtos; // Lista de produtos associados à encomenda

    private List<EmbalagemDeTransporteDTO> embalagensTransporte;
    private String operadorLogistica;
    private String estado;

    public EncomendaDTO() {
        this.produtos = new ArrayList<>();
        this.embalagensTransporte = new ArrayList<>();
    }

    public EncomendaDTO(Long id, String consumidorFinal, String dataEncomenda, String operadorLogistica, String estado) {
        this.id = id;
        this.consumidorFinal = consumidorFinal;
        this.dataEncomenda = dataEncomenda;
        this.operadorLogistica = operadorLogistica;
        this.estado = estado;
        this.produtos = new ArrayList<>();
        this.embalagensTransporte = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConsumidorFinal() {
        return consumidorFinal;
    }

    public void setConsumidorFinal(String consumidorFinal) {
        this.consumidorFinal = consumidorFinal;
    }

    public String getDataEncomenda() {
        return dataEncomenda;
    }

    public void setDataEncomenda(String dataEncomenda) {
        this.dataEncomenda = dataEncomenda;
    }

    public List<ProdutoFisicoDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoFisicoDTO> produtos) {
        this.produtos = produtos;
    }

    public String getOperadorLogistica() {
        return operadorLogistica;
    }

    public void setOperadorLogistica(String operadorLogistica) {
        this.operadorLogistica = operadorLogistica;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<EmbalagemDeTransporteDTO> getEmbalagensTransporte() {
        return embalagensTransporte;
    }

    public void setEmbalagensTransporte(List<EmbalagemDeTransporteDTO> embalagensTransporte) {
        this.embalagensTransporte = embalagensTransporte;
    }
}
