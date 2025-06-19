package br.ufscar.dc.dsw.model;

import br.ufscar.dc.dsw.model.enums.Severity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity(name = "Bug")
@Table(name = "bug")
public class Bug {

    @Id
    @SequenceGenerator(
            name = "bug_sequence",
            sequenceName = "bug_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "bug_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessao_id", nullable = false)
    private Sessao sessao;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "severidade",
            nullable = false
    )
    private Severity severidade;

    @Column(
            name = "descricao",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String descricao;

    @Column(
            name = "timestamp",
            nullable = false,
            updatable = false
    )
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    public Bug() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Sessao getSessao() {
        return sessao;
    }

    public void setSessao(Sessao sessao) {
        this.sessao = sessao;
    }

    public Severity getSeveridade() {
        return severidade;
    }

    public void setSeveridade(Severity severidade) {
        this.severidade = severidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}