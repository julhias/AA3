package br.ufscar.dc.dsw.services;

import java.util.List;

import br.ufscar.dc.dsw.dtos.EstrategiaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufscar.dc.dsw.model.Estrategia;
import br.ufscar.dc.dsw.repositories.EstrategiaRepository;
import br.ufscar.dc.dsw.repositories.SessaoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class EstrategiaService {

    @Autowired
    private EstrategiaRepository estrategiaRepository;
    
    @Autowired
    private SessaoRepository sessaoRepository;

    public Estrategia salvar(Estrategia estrategia) {
        return estrategiaRepository.save(estrategia);
    }

    public Estrategia atualizarTotalmente(Integer id, Estrategia dadosParaAtualizar) {
        this.buscarPorId(id);

        dadosParaAtualizar.setId(id);
        return estrategiaRepository.save(dadosParaAtualizar);
    }

    public Estrategia atualizarParcialmente(Integer id, EstrategiaDTO dto) {
        Estrategia estrategiaAtual = this.buscarPorId(id);

        if (dto.getNome() != null && !dto.getNome().isBlank()) {
            estrategiaAtual.setNome(dto.getNome());
        }

        if (dto.getDescricao() != null && !dto.getDescricao().isBlank()) {
            estrategiaAtual.setDescricao(dto.getDescricao());
        }

        return estrategiaRepository.save(estrategiaAtual);
    }

    @Transactional(readOnly = true)
    public List<Estrategia> buscarTodos() { // <-- MÉTODO QUE ESTAVA FALTANDO
        return estrategiaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Estrategia buscarPorId(Integer id) {
        return estrategiaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estratégia não encontrada com o ID: " + id));
    }

    public void excluir(Integer id) {
        Estrategia estrategia = this.buscarPorId(id);
        if (sessaoRepository.existsByEstrategia(estrategia)) {
            throw new IllegalStateException("Não é possível excluir a estratégia, pois ela está associada a uma ou mais sessões.");
        }
        estrategiaRepository.deleteById(id);
    }
}