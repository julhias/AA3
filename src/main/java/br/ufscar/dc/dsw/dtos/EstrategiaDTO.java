package br.ufscar.dc.dsw.dtos;

public class EstrategiaDTO {
    private Integer id;
    private String nome;
    private String descricao;
    private String exemplos;
    private String dicas;

    // GETTERS E SETTERS
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getExemplos() { return exemplos; }
    public void setExemplos(String exemplos) { this.exemplos = exemplos; }
    public String getDicas() { return dicas; }
    public void setDicas(String dicas) { this.dicas = dicas; }
}