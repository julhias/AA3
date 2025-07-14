package br.ufscar.dc.dsw.dtos;

public class SessaoCreateDTO {
    private String titulo;
    private String descricao;
    private Integer tempoDefinido;
    private Integer projetoId;
    private Integer estrategiaId;

    // GETTERS E SETTERS
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Integer getTempoDefinido() { return tempoDefinido; }
    public void setTempoDefinido(Integer tempoDefinido) { this.tempoDefinido = tempoDefinido; }
    public Integer getProjetoId() { return projetoId; }
    public void setProjetoId(Integer projetoId) { this.projetoId = projetoId; }
    public Integer getEstrategiaId() { return estrategiaId; }
    public void setEstrategiaId(Integer estrategiaId) { this.estrategiaId = estrategiaId; }
}