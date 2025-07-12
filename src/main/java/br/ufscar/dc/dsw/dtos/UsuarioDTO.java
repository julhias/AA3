package br.ufscar.dc.dsw.dto;

import br.ufscar.dc.dsw.model.enums.Role;

public class UsuarioDTO {
    private Long id;
    private String nome;
    private String login;
    private String senha; // Usado para input (criar/atualizar), mas não para output
    private Role tipo;

    // GETTERS E SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public Role getTipo() { return tipo; }
    public void setTipo(Role tipo) { this.tipo = tipo; }
}