package br.ufscar.dc.dsw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity(name = "Projeto")
@Table(
        name = "projeto",
        uniqueConstraints = {
                @UniqueConstraint(name = "projeto_nome_unique", columnNames = "nome")
        }
)
public class Projeto {

    @Id
    @SequenceGenerator(
            name = "projeto_sequence",
            sequenceName = "projeto_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "projeto_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Integer id;

    @NotBlank(message = "{NotBlank.projeto.nome}")
    @Column(
            name = "nome",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String nome;

    @Column(
            name = "descricao",
            columnDefinition = "TEXT"
    )
    private String descricao;

    @Column(
            name = "criado_em",
            nullable = false,
            updatable = false
    )
    private LocalDateTime criadoEm;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "projeto_usuario",
            joinColumns = @JoinColumn(name = "projeto_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> usuarios = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
    }

    public Projeto() {
    }

    public Projeto(String nome, String descricao) { // Added a new constructor for convenience
        this.nome = nome;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public void adicionarUsuario(Usuario usuario) {
        if (this.usuarios == null) {
            this.usuarios = new ArrayList<>();
        }
        this.usuarios.add(usuario);
    }
}