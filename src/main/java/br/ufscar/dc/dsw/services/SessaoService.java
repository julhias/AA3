package br.ufscar.dc.dsw.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufscar.dc.dsw.dtos.BugDTO;
import br.ufscar.dc.dsw.dtos.SessaoCreateDTO;
import br.ufscar.dc.dsw.mapper.EntityMapper;
import br.ufscar.dc.dsw.model.Bug;
import br.ufscar.dc.dsw.model.Estrategia;
import br.ufscar.dc.dsw.model.Projeto;
import br.ufscar.dc.dsw.model.Sessao;
import br.ufscar.dc.dsw.model.Usuario;
import br.ufscar.dc.dsw.model.enums.SessionStatus;
import br.ufscar.dc.dsw.repositories.BugRepository;
import br.ufscar.dc.dsw.repositories.EstrategiaRepository;
import br.ufscar.dc.dsw.repositories.ProjetoRepository;
import br.ufscar.dc.dsw.repositories.SessaoRepository; // Correto: você está usando SessaoRepository
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class SessaoService {

    @Autowired private SessaoRepository sessaoRepository;
    @Autowired private BugRepository bugRepository;
    @Autowired private ProjetoRepository projetoRepository;
    @Autowired private EstrategiaRepository estrategiaRepository;
    @Autowired private EntityMapper mapper;

    public Sessao criarSessao(SessaoCreateDTO dto, Usuario testador) {
        Sessao sessao = mapper.toEntity(dto);

        Projeto projeto = projetoRepository.findById(dto.getProjetoId())
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado com o ID: " + dto.getProjetoId()));
        Estrategia estrategia = estrategiaRepository.findById(dto.getEstrategiaId())
                .orElseThrow(() -> new EntityNotFoundException("Estratégia não encontrada com o ID: " + dto.getEstrategiaId()));

        sessao.setProjeto(projeto);
        sessao.setEstrategia(estrategia);
        sessao.setTestador(testador);
        sessao.setStatus(SessionStatus.CRIADA);
        sessao.setCriadoEm(LocalDateTime.now());

        return sessaoRepository.save(sessao);
    }

    public void iniciarSessao(Integer id, String username) {
        Sessao sessao = this.buscarPorId(id);
        verificarDono(sessao, username, "iniciar");

        if (sessao.getStatus() != SessionStatus.CRIADA) {
            throw new IllegalStateException("Apenas sessões com status 'CRIADA' podem ser iniciadas.");
        }
        sessao.setStatus(SessionStatus.EM_ANDAMENTO);
        sessao.setInicioEm(LocalDateTime.now());
        sessaoRepository.save(sessao);
    }


    public void finalizarSessao(Integer id, String username) {
        Sessao sessao = this.buscarPorId(id);
        verificarDono(sessao, username, "finalizar");

        if (sessao.getStatus() != SessionStatus.EM_ANDAMENTO) {
            throw new IllegalStateException("Apenas sessões 'EM ANDAMENTO' podem ser finalizadas.");
        }
        sessao.setStatus(SessionStatus.FINALIZADA);
        sessao.setFinalizadoEm(LocalDateTime.now());
        sessaoRepository.save(sessao);
    }

    public Bug adicionarBug(Integer sessaoId, BugDTO bugDto, String username) {
        Sessao sessao = this.buscarPorId(sessaoId);
        verificarDono(sessao, username, "adicionar bugs");

        if (sessao.getStatus() != SessionStatus.EM_ANDAMENTO) {
            throw new IllegalStateException("Bugs só podem ser adicionados a sessões 'EM ANDAMENTO'.");
        }
        Bug bug = mapper.toEntity(bugDto);
        bug.setSessao(sessao);
        bug.setTimestamp(LocalDateTime.now()); // Adicionei esta linha para registrar o timestamp do bug
        return bugRepository.save(bug);
    }

    private void verificarDono(Sessao sessao, String username, String acao) {
        if (sessao.getTestador() == null || !sessao.getTestador().getLogin().equals(username)) {
            throw new AccessDeniedException(String.format("Acesso negado. Você não tem permissão para %s esta sessão.", acao));
        }
    }

    @Transactional(readOnly = true)
    public Sessao buscarPorId(Integer id) {
        return sessaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Sessao> buscarPorTestador(Usuario testador) {
        return sessaoRepository.findByTestadorOrderByCriadoEmDesc(testador);
    }

    @Transactional(readOnly = true)
    public List<Bug> buscarBugsPorSessao(Integer sessaoId) {
        return bugRepository.findBySessaoIdOrderByTimestampDesc(sessaoId);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void finalizarSessoesAutomaticamente() {
        LocalDateTime limiteTempo = LocalDateTime.now().minusHours(1);

        List<Sessao> sessoesParaFinalizar = sessaoRepository.findByStatusAndInicioEmBefore(
                SessionStatus.EM_ANDAMENTO,
                limiteTempo
        );

        if (!sessoesParaFinalizar.isEmpty()) {
            System.out.println("Finalizando automaticamente " + sessoesParaFinalizar.size() + " sessões expiradas...");
            for (Sessao sessao : sessoesParaFinalizar) {
                sessao.setStatus(SessionStatus.FINALIZADA);
                sessao.setFinalizadoEm(LocalDateTime.now());
                sessaoRepository.save(sessao);
            }
            System.out.println("Finalização automática concluída.");
        } else {
            System.out.println("Nenhuma sessão 'EM_ANDAMENTO' expirada para finalizar automaticamente.");
        }
    }
}