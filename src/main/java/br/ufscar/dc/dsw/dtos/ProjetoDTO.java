package br.ufscar.dc.dsw.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class ProjetoDTO {
    private Integer id;
    private String nome;
    private String descricao;
    private LocalDateTime criadoEm;
    private List<Long> testadoresIds;

    // GETTERS E SETTERS
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public List<Long> getTestadoresIds() { return testadoresIds; }
    public void setTestadoresIds(List<Long> testadoresIds) { this.testadoresIds = testadoresIds; }
}