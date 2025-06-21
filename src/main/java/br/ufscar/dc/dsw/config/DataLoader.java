package br.ufscar.dc.dsw.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.ufscar.dc.dsw.model.Usuario;
import br.ufscar.dc.dsw.model.enums.Role;
import br.ufscar.dc.dsw.repositories.UsuarioRepository;

// @Configuration indica ao Spring que esta é uma classe de configuração.
@Configuration
public class DataLoader {

    // @Bean cria um "componente" que o Spring gerencia.
    // CommandLineRunner é um tipo especial de bean que executa seu código
    // uma única vez, logo após a aplicação iniciar.
    @Bean
    public CommandLineRunner initialData(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            // 1. Verificamos se já existe algum usuário no banco.
            // Se sim, não fazemos nada para não duplicar dados.
            if (usuarioRepository.count() > 0) {
                return;
            }

            System.out.println(">>> Executando DataLoader: Inserindo dados iniciais...");

            // 2. Criamos o usuário admin, criptografando a senha "admin".
            Usuario admin = new Usuario("Administrador", "admin@admin.com", passwordEncoder.encode("admin"), Role.ADMIN);
            usuarioRepository.save(admin);
            
            // 3. Criamos o usuário tester, criptografando a senha "tester".
            Usuario tester = new Usuario("Fulano de Tal", "tester@email.com", passwordEncoder.encode("tester"), Role.TESTER);
            usuarioRepository.save(tester);

            System.out.println(">>> DataLoader finalizado: Usuário admin e tester criados.");
        };
    }
}