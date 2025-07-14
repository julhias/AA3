// Define o pacote onde essa classe está localizada (organização do projeto)
package br.ufscar.dc.dsw.controllers;

// Importa os DTOs usados para receber/enviar dados de sessão e bug
import br.ufscar.dc.dsw.dtos.BugDTO;
import br.ufscar.dc.dsw.dtos.SessaoCreateDTO;
import br.ufscar.dc.dsw.dtos.SessaoDTO;

// Importa o mapper usado para converter entidade - DTO
import br.ufscar.dc.dsw.mapper.EntityMapper;

// Importa as entidades do banco
import br.ufscar.dc.dsw.model.Bug;
import br.ufscar.dc.dsw.model.Sessao;
import br.ufscar.dc.dsw.model.Usuario;

// Importa os serviços que contêm a lógica de negócio
import br.ufscar.dc.dsw.services.SessaoService;
import br.ufscar.dc.dsw.services.UsuarioService;

// Importa a anotação para validação dos DTOs
import jakarta.validation.Valid;

// Importa anotações e classes do Spring para REST, segurança, HTTP
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

// Importa utilitários para trabalhar com URLs e listas
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

// Declara a classe como um controller REST que responde com JSON
@RestController
// Define que todos os endpoints desse controller começam com /api/sessoes
@RequestMapping("/api/sessoes")
public class SessaoController {

    // Injeta o serviço de sessão (Spring cuida da criação e gerenciamento)
    @Autowired
    private SessaoService sessaoService;

    // Injeta o serviço de usuário
    @Autowired
    private UsuarioService usuarioService;

    // Injeta o mapper que converte entidade ↔ DTO
    @Autowired
    private EntityMapper mapper;

    // -------------------- ENDPOINT: Buscar uma sessão por ID --------------------
    @GetMapping("/{id}")
    public ResponseEntity<SessaoDTO> buscarSessao(@PathVariable Integer id) {
        // Busca a sessão pelo ID usando o serviço
        Sessao sessao = sessaoService.buscarPorId(id);
        // Converte a entidade para DTO e retorna com status 200 OK
        return ResponseEntity.ok(mapper.toDTO(sessao));
    }

    // -------------------- ENDPOINT: Criar uma nova sessão --------------------
    @PostMapping
    public ResponseEntity<SessaoDTO> criarSessao(
            @Valid @RequestBody SessaoCreateDTO dto, // Recebe o corpo da requisição como DTO e valida
            @AuthenticationPrincipal UserDetails userDetails // Pega o usuário autenticado
    ) {
        // Busca o objeto Usuario correspondente ao login autenticado
        Usuario testador = usuarioService.buscarPorLogin(userDetails.getUsername());

        // Cria uma nova sessão com base nos dados recebidos e no testador logado
        Sessao novaSessao = sessaoService.criarSessao(dto, testador);

        // Constrói a URI do recurso criado
        URI location = URI.create(String.format("/api/sessoes/%d", novaSessao.getId()));

        // Retorna status 201 Created com a URI e o corpo contendo o novo DTO
        return ResponseEntity.created(location).body(mapper.toDTO(novaSessao));
    }

    // -------------------- ENDPOINT: Iniciar uma sessão --------------------
    @PostMapping("/{id}/iniciar")
    public ResponseEntity<Void> iniciarSessao(
            @PathVariable Integer id, // ID da sessão a ser iniciada
            @AuthenticationPrincipal UserDetails userDetails // Usuário autenticado
    ) {
        // Chama o serviço para iniciar a sessão com base no ID e login do usuário
        sessaoService.iniciarSessao(id, userDetails.getUsername());

        // Retorna 200 OK sem corpo (Void)
        return ResponseEntity.ok().build();
    }

    // -------------------- ENDPOINT: Finalizar uma sessão --------------------
    @PostMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizarSessao(
            @PathVariable Integer id, // ID da sessão a ser finalizada
            @AuthenticationPrincipal UserDetails userDetails // Usuário autenticado
    ) {
        // Chama o serviço para finalizar a sessão com base no ID e login
        sessaoService.finalizarSessao(id, userDetails.getUsername());

        // Retorna 200 OK sem corpo
        return ResponseEntity.ok().build();
    }

    // -------------------- ENDPOINT: Listar todos os bugs de uma sessão --------------------
    @GetMapping("/{sessaoId}/bugs")
    public ResponseEntity<List<BugDTO>> listarBugsDaSessao(@PathVariable Integer sessaoId) {
        // Busca os bugs da sessão pelo ID e converte cada um para DTO
        List<BugDTO> dtos = sessaoService.buscarBugsPorSessao(sessaoId)
                .stream()
                .map(mapper::toDTO) // Converte Bug → BugDTO
                .collect(Collectors.toList());

        // Retorna 200 OK com a lista de bugs em formato DTO
        return ResponseEntity.ok(dtos);
    }

    // -------------------- ENDPOINT: Adicionar um bug à sessão --------------------
    @PostMapping("/{sessaoId}/bugs")
    public ResponseEntity<BugDTO> adicionarBug(
            @PathVariable Integer sessaoId, // ID da sessão
            @Valid @RequestBody BugDTO bugDto, // Dados do bug vindo do corpo da requisição
            @AuthenticationPrincipal UserDetails userDetails // Usuário autenticado
    ) {
        // Adiciona o bug na sessão e associa ao usuário logado
        Bug novoBug = sessaoService.adicionarBug(sessaoId, bugDto, userDetails.getUsername());

        // Converte o novo bug salvo em DTO
        BugDTO novoBugDto = mapper.toDTO(novoBug);

        // Cria a URI do novo recurso
        URI location = URI.create(String.format("/api/sessoes/%d/bugs/%d", sessaoId, novoBugDto.getId()));

        // Retorna 201 Created com o corpo do novo bug em DTO
        return ResponseEntity.created(location).body(novoBugDto);
    }
}
