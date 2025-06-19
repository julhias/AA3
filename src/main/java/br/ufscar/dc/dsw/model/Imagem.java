package br.ufscar.dc.dsw.model;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity(name = "Imagem")
@Table(
        name = "imagem",
        uniqueConstraints = {
                @UniqueConstraint(name = "imagem_url_unique", columnNames = "url")
        }
)
public class Imagem {

    @Id
    @SequenceGenerator(
            name = "imagem_sequence",
            sequenceName = "imagem_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "imagem_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Integer id;

    @Column(
            name = "url",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String url;

    @Column(
            name = "descricao",
            columnDefinition = "TEXT"
    )
    private String descricao;


    public Imagem() {
    }

    public Imagem(String url, String descricao) {
        this.url = url;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}