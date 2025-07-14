package br.ufscar.dc.dsw.dtos;

import java.time.LocalDateTime;

import br.ufscar.dc.dsw.model.enums.SessionStatus;

public class SessaoDTO {
    private Integer id;
    private String titulo;
    private String descricao;
    private Integer tempoDefinido;
    private SessionStatus status;
    private LocalDateTime criadoEm;
    private LocalDateTime inicioEm;
    private LocalDateTime finalizadoEm;
    private Integer projetoId;
    private Long testadorId;
    private Integer estrategiaId;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Integer getTempoDefinido() { return tempoDefinido; }
    public void setTempoDefinido(Integer tempoDefinido) { this.tempoDefinido = tempoDefinido; }
    public SessionStatus getStatus() { return status; }
    public void setStatus(SessionStatus status) { this.status = status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public LocalDateTime getInicioEm() { return inicioEm; }
    public void setInicioEm(LocalDateTime inicioEm) { this.inicioEm = inicioEm; }
    public LocalDateTime getFinalizadoEm() { return finalizadoEm; }
    public void setFinalizadoEm(LocalDateTime finalizadoEm) { this.finalizadoEm = finalizadoEm; }
    public Integer getProjetoId() { return projetoId; }
    public void setProjetoId(Integer projetoId) { this.projetoId = projetoId; }
    public Long getTestadorId() { return testadorId; }
    public void setTestadorId(Long testadorId) { this.testadorId = testadorId; }
    public Integer getEstrategiaId() { return estrategiaId; }
    public void setEstrategiaId(Integer estrategiaId) { this.estrategiaId = estrategiaId; }
}