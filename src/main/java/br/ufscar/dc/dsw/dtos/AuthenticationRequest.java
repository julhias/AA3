package br.ufscar.dc.dsw.dtos;

public class AuthenticationRequest {

    private String login;
    private String senha;

    public AuthenticationRequest() {}

    public AuthenticationRequest(String username, String password) {
        this.login = username;
        this.senha = password;
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
}