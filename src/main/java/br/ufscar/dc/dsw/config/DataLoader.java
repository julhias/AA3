package br.ufscar.dc.dsw.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.ufscar.dc.dsw.model.Usuario;
import br.ufscar.dc.dsw.model.enums.Role;
import br.ufscar.dc.dsw.repositories.UsuarioRepository;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner initialData(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (usuarioRepository.count() > 0) {
                return;
            }

            System.out.println(">>> Executando DataLoader: Inserindo dados iniciais...");

            Usuario admin = new Usuario("Administrador", "admin@admin.com", passwordEncoder.encode("admin"), Role.ADMIN);
            usuarioRepository.save(admin);
            
            // 3. Criamos o usuário tester, criptografando a senha "tester".
            Usuario tester = new Usuario("Fulano de Tal", "tester@email.com", passwordEncoder.encode("tester"), Role.TESTER);
            usuarioRepository.save(tester);

            System.out.println(">>> DataLoader finalizado: Usuário admin e tester criados.");
        };
    }
}