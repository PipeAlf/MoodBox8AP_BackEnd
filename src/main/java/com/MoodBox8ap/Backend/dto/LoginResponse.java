package com.MoodBox8ap.Backend.dto;

import com.MoodBox8ap.Backend.model.Usuario;

public class LoginResponse {
    private String accessToken;
    private Usuario usuario;

    public LoginResponse(String accessToken, Usuario usuario) {
        this.accessToken = accessToken;
        this.usuario = usuario;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
