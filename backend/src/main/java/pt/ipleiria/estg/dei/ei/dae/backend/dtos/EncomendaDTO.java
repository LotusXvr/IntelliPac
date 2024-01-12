package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EncomendaDTO{
    private Long id;
    private String consumidorFinal;
    private Date dataEncomenda;
    private List<ProdutoFisicoDTO> produtos; // Lista de produtos associados à encomenda

    public EncomendaDTO() {
        this.produtos = new ArrayList<>();
    }

    public EncomendaDTO(Long id, String consumidorFinal, Date dataEncomenda, List<ProdutoFisicoDTO> produtos) {
        this.id = id;
        this.consumidorFinal = consumidorFinal;
        this.dataEncomenda = dataEncomenda;
        this.produtos = produtos;
    }

    public EncomendaDTO(Long id, String consumidorFinal, Date dataEncomenda) {
        this.id = id;
        this.consumidorFinal = consumidorFinal;
        this.dataEncomenda = dataEncomenda;
        this.produtos = new ArrayList<>();
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

    public Date getDataEncomenda() {
        return dataEncomenda;
    }

    public void setDataEncomenda(Date dataEncomenda) {
        this.dataEncomenda = dataEncomenda;
    }

    public List<ProdutoFisicoDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoFisicoDTO> produtos) {
        this.produtos = produtos;
    }
}
