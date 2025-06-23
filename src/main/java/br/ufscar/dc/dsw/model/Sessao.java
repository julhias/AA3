package br.ufscar.dc.dsw.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.ufscar.dc.dsw.model.enums.SessionStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.SEQUENCE;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity(name = "Sessao")
@Table(name = "sessao")
public class Sessao {

    // --- ID ---
    @Id
    @SequenceGenerator(
            name = "sessao_sequence",
            sequenceName = "sessao_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "sessao_sequence"
    )
    @Column(name = "id", updatable = false)
    private Integer id;

    // --- Colunas Simples ---
    @Column(name = "titulo", nullable = false, columnDefinition = "TEXT")
    private String titulo;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
    
    @Column(name = "tempo_definido", nullable = false)
    private Integer tempoDefinido;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SessionStatus status;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "inicio_em")
    private LocalDateTime inicioEm;

    @Column(name = "finalizado_em")
    private LocalDateTime finalizadoEm;

    // --- Relacionamentos Many-to-One ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "testador_id", nullable = false)
    private Usuario testador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estrategia_id", nullable = false)
    private Estrategia estrategia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    // --- Relacionamentos One-to-Many ---
    @OneToMany(
            mappedBy = "sessao", // "sessao" é o nome do campo na classe Bug
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Bug> bugs = new ArrayList<>(); // << DECLARAÇÃO ÚNICA E CORRETA

    // --- Métodos de Ciclo de Vida ---
    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        if (status == null) {
            status = SessionStatus.CRIADA;
        }
    }

    // --- Construtor Padrão (requerido pelo JPA) ---
    public Sessao() {
    }

    // --- Getters e Setters ---
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getTempoDefinido() {
        return tempoDefinido;
    }

    public void setTempoDefinido(Integer tempoDefinido) {
        this.tempoDefinido = tempoDefinido;
    }
    
    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getInicioEm() {
        return inicioEm;
    }

    public void setInicioEm(LocalDateTime inicioEm) {
        this.inicioEm = inicioEm;
    }

    public LocalDateTime getFinalizadoEm() {
        return finalizadoEm;
    }

    public void setFinalizadoEm(LocalDateTime finalizadoEm) {
        this.finalizadoEm = finalizadoEm;
    }

    public Usuario getTestador() {
        return testador;
    }

    public void setTestador(Usuario testador) {
        this.testador = testador;
    }

    public Estrategia getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(Estrategia estrategia) {
        this.estrategia = estrategia;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public List<Bug> getBugs() {
        return bugs;
    }

    public void setBugs(List<Bug> bugs) {
        this.bugs = bugs;
    }
}