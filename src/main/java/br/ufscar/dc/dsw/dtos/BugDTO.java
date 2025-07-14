package br.ufscar.dc.dsw.dtos;

import java.time.LocalDateTime;

import br.ufscar.dc.dsw.model.enums.Severity;

public class BugDTO {
    private Integer id;
    private String descricao;
    private Severity severidade;
    private LocalDateTime timestamp;
    private Integer sessaoId;

    // GETTERS E SETTERS
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Severity getSeveridade() { return severidade; }
    public void setSeveridade(Severity severidade) { this.severidade = severidade; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Integer getSessaoId() { return sessaoId; }
    public void setSessaoId(Integer sessaoId) { this.sessaoId = sessaoId; }
}