package br.ufscar.dc.dsw.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity(name = "Estrategia")
@Table(
        name = "estrategia",
        uniqueConstraints = {
                @UniqueConstraint(name = "estrategia_nome_unique", columnNames = "nome")
        }
)
public class Estrategia {

    @Id
    @SequenceGenerator(
            name = "estrategia_sequence",
            sequenceName = "estrategia_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "estrategia_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Integer id;

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
            name = "exemplos",
            columnDefinition = "TEXT"
    )
    private String exemplos;

    @Column(
            name = "dicas",
            columnDefinition = "TEXT"
    )
    private String dicas;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinTable(
            name = "estrategia_imagem",
            joinColumns = @JoinColumn(name = "estrategia_id"),
            inverseJoinColumns = @JoinColumn(name = "imagem_id")
    )
    private List<Imagem> imagens = new ArrayList<>();


    public Estrategia() {
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

    public String getExemplos() {
        return exemplos;
    }

    public void setExemplos(String exemplos) {
        this.exemplos = exemplos;
    }

    public String getDicas() {
        return dicas;
    }

    public void setDicas(String dicas) {
        this.dicas = dicas;
    }

    public List<Imagem> getImagens() {
        return imagens;
    }

    public void setImagens(List<Imagem> imagens) {
        this.imagens = imagens;
    }

    public void adicionarImagem(Imagem imagem) {
        if (this.imagens == null) {
            this.imagens = new ArrayList<>();
        }
        this.imagens.add(imagem);
    }
}