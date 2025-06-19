package br.ufscar.dc.dsw.utils; // Ou o pacote onde você tem suas classes utilitárias

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AdminPasswordGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Senha original (admin): " + rawPassword);
        System.out.println("Senha criptografada (BCrypt): " + encodedPassword);
    }
}
