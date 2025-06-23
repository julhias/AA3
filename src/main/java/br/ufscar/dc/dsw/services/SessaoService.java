package br.ufscar.dc.dsw.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled; // Added import

import br.ufscar.dc.dsw.model.Bug;
import br.ufscar.dc.dsw.model.Sessao;
import br.ufscar.dc.dsw.model.Usuario;
import br.ufscar.dc.dsw.model.enums.SessionStatus;
import br.ufscar.dc.dsw.repositories.BugRepository;
import br.ufscar.dc.dsw.repositories.SessaoRepository;

@Service
@Transactional
public class SessaoService {

    private final SessaoRepository sessaoRepository;
    private final BugRepository bugRepository;

    public SessaoService(SessaoRepository sessaoRepository, BugRepository bugRepository) {
        this.sessaoRepository = sessaoRepository;
        this.bugRepository = bugRepository;
    }

    public Sessao salvar(Sessao sessao) {
        // Ao salvar uma nova sessão, garanta que o status inicial seja CRIADA
        if (sessao.getId() == null) { // É uma nova sessão
            sessao.setStatus(SessionStatus.CRIADA);
            sessao.setCriadoEm(LocalDateTime.now());
        }
        return sessaoRepository.save(sessao);
    }

    @Transactional(readOnly = true)
    public Sessao buscarPorId(Integer id) {
        return sessaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Sessao> buscarPorProjeto(Integer projetoId) {
        return sessaoRepository.findByProjetoIdOrderByCriadoEmDesc(projetoId);
    }

    @Transactional(readOnly = true)
    public List<Sessao> buscarPorTestador(Usuario testador) {
        return sessaoRepository.findByTestadorOrderByCriadoEmDesc(testador);
    }

    // Inicia o ciclo de vida (R8)
    public void iniciarSessao(Integer id) {
        Sessao sessao = buscarPorId(id);
        if (sessao.getStatus() != SessionStatus.CRIADA) {
            throw new IllegalStateException("Apenas sessões com status 'CRIADA' podem ser iniciadas.");
        }
        sessao.setStatus(SessionStatus.EM_ANDAMENTO);
        sessao.setInicioEm(LocalDateTime.now());
        sessaoRepository.save(sessao);
    }

    // Finaliza o ciclo de vida (R8)
    public void finalizarSessao(Integer id) {
        Sessao sessao = buscarPorId(id);
        if (sessao.getStatus() != SessionStatus.EM_ANDAMENTO) {
            throw new IllegalStateException("Apenas sessões 'EM ANDAMENTO' podem ser finalizadas.");
        }
        sessao.setStatus(SessionStatus.FINALIZADA);
        sessao.setFinalizadoEm(LocalDateTime.now());
        sessaoRepository.save(sessao);
    }

    // Registra um bug durante a execução (R8)
    public void adicionarBug(Integer sessaoId, Bug bug) {
        Sessao sessao = buscarPorId(sessaoId);
        if (sessao.getStatus() != SessionStatus.EM_ANDAMENTO) {
            throw new IllegalStateException("Bugs só podem ser adicionados a sessões 'EM ANDAMENTO'.");
        }
        bug.setSessao(sessao);
        bug.setTimestamp(LocalDateTime.now());
        bugRepository.save(bug);
    }

    public Sessao editar(Sessao sessao) {
        Sessao sessaoExistente = this.buscarPorId(sessao.getId());
        sessaoExistente.setTitulo(sessao.getTitulo());
        sessaoExistente.setDescricao(sessao.getDescricao());
        sessaoExistente.setTempoDefinido(sessao.getTempoDefinido());
        sessaoExistente.setEstrategia(sessao.getEstrategia());
        return sessaoRepository.save(sessaoExistente);
    }

    public void excluir(Integer id) {
        Sessao sessao = this.buscarPorId(id);
        if (sessao.getStatus() == SessionStatus.EM_ANDAMENTO) {
            throw new IllegalStateException("Não é possível excluir uma sessão que está em andamento.");
        }
        List<Bug> bugsDaSessao = bugRepository.findBySessaoIdOrderByTimestampDesc(id);
        if (!bugsDaSessao.isEmpty()) {
            bugRepository.deleteAll(bugsDaSessao);
        }
        sessaoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Bug> buscarBugsPorSessao(Integer sessaoId) {
        return bugRepository.findBySessaoIdOrderByTimestampDesc(sessaoId);
    }

    // Novo método para finalizar sessões automaticamente
    @Scheduled(fixedRate = 60000) // Executa a cada 1 minuto (60000 ms)
    public void finalizarSessoesAutomaticamente() {
        System.out.println("Verificando sessões para finalização automática...");
        List<Sessao> sessoesEmAndamento = sessaoRepository.findByStatus(SessionStatus.EM_ANDAMENTO);
        LocalDateTime agora = LocalDateTime.now();

        for (Sessao sessao : sessoesEmAndamento) {
            if (sessao.getInicioEm() != null && sessao.getTempoDefinido() != null) {
                LocalDateTime tempoLimite = sessao.getInicioEm().plusMinutes(sessao.getTempoDefinido());
                if (agora.isAfter(tempoLimite)) {
                    System.out.println("Finalizando sessão " + sessao.getId() + " automaticamente. Tempo definido excedido.");
                    finalizarSessao(sessao.getId());
                }
            }
        }
    }
}