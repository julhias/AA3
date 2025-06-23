package br.ufscar.dc.dsw.model;

import br.ufscar.dc.dsw.model.enums.Role;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity(name = "Usuario")
@Table(
        name = "usuario",
        uniqueConstraints = {
                @UniqueConstraint(name = "usuario_login_unique", columnNames = "login")
        }
)
public class Usuario {

    @Id
    @SequenceGenerator(
            name = "usuario_sequence",
            sequenceName = "usuario_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "usuario_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "nome",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String nome;

    @Column(
            name = "login",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String login;

    @Column(
            name = "senha",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "tipo",
            nullable = false
    )
    private Role tipo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "projeto_usuario",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "projeto_id")
    )
    private List<Projeto> projetos = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(String nome, String login, String senha, Role tipo) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Role getTipo() {
        return tipo;
    }

    public void setTipo(Role tipo) {
        this.tipo = tipo;
    }

    public List<Projeto> getProjetos() {
        return projetos;
    }

    public void setProjetos(List<Projeto> projetos) {
        this.projetos = projetos;
    }

    public void adicionarProjeto(Projeto projeto) {
        if (this.projetos == null) {
            this.projetos = new ArrayList<>();
        }
        this.projetos.add(projeto);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", login='" + login + '\'' +
                ", senha='" + "********" + '\'' +
                ", tipo=" + tipo +
                '}';
    }
}