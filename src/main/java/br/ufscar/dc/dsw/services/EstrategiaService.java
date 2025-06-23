package br.ufscar.dc.dsw.services;

import br.ufscar.dc.dsw.model.Estrategia;
import br.ufscar.dc.dsw.repositories.EstrategiaRepository;
import br.ufscar.dc.dsw.repositories.SessaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstrategiaService {

    private final EstrategiaRepository estrategiaRepository;
    private final SessaoRepository sessaoRepository;

    public EstrategiaService(EstrategiaRepository estrategiaRepository, SessaoRepository sessaoRepository) {
        this.estrategiaRepository = estrategiaRepository;
        this.sessaoRepository = sessaoRepository;
    }

    @Transactional
    public Estrategia salvar(Estrategia estrategia) {
        return estrategiaRepository.save(estrategia);
    }

    @Transactional(readOnly = true)
    public List<Estrategia> buscarTodas() {
        return estrategiaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Estrategia buscarPorId(Integer id) {
        return estrategiaRepository.findById(id).orElse(null);
    }

    @Transactional
    public void excluir(Integer id) {
        Estrategia estrategia = buscarPorId(id);
        if (estrategia == null) {
            throw new IllegalArgumentException("ID de estratégia inválido:" + id);
        }
        if (sessaoRepository.existsByEstrategia(estrategia)) {
            throw new RuntimeException("Não é possível excluir a estratégia, pois ela está associada a uma ou mais sessões de teste.");
        }
        estrategiaRepository.deleteById(id);
    }
}