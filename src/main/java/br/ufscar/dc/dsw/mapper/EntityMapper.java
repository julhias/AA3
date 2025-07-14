package br.ufscar.dc.dsw.mapper;

import br.ufscar.dc.dsw.dtos.*;
import br.ufscar.dc.dsw.model.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class EntityMapper {

    public ProjetoDTO toDTO(Projeto projeto) {
        if (projeto == null) {
            return null;
        }
        ProjetoDTO dto = new ProjetoDTO();
        dto.setId(projeto.getId());
        dto.setNome(projeto.getNome());
        dto.setDescricao(projeto.getDescricao());
        dto.setCriadoEm(projeto.getCriadoEm());

        if (projeto.getUsuarios() != null) {
            dto.setTestadoresIds(projeto.getUsuarios().stream()
                .map(Usuario::getId)
                .collect(Collectors.toList()));
        } else {
            dto.setTestadoresIds(Collections.emptyList());
        }

        return dto;
    }

    public Projeto toEntity(ProjetoDTO dto) {
        if (dto == null) {
            return null;
        }
        Projeto projeto = new Projeto();
        projeto.setId(dto.getId());
        projeto.setNome(dto.getNome());
        projeto.setDescricao(dto.getDescricao());
        return projeto;
    }

    public EstrategiaDTO toDTO(Estrategia estrategia) {
        if (estrategia == null) {
            return null;
        }
        EstrategiaDTO dto = new EstrategiaDTO();
        dto.setId(estrategia.getId());
        dto.setNome(estrategia.getNome());
        dto.setDescricao(estrategia.getDescricao());
        dto.setExemplos(estrategia.getExemplos());
        dto.setDicas(estrategia.getDicas());
        return dto;
    }

    public Estrategia toEntity(EstrategiaDTO dto) {
        if (dto == null) {
            return null;
        }
        Estrategia estrategia = new Estrategia();
        estrategia.setId(dto.getId());
        estrategia.setNome(dto.getNome());
        estrategia.setDescricao(dto.getDescricao());
        estrategia.setExemplos(dto.getExemplos());
        estrategia.setDicas(dto.getDicas());
        return estrategia;
    }


    public SessaoDTO toDTO(Sessao sessao) {
        if (sessao == null) {
            return null;
        }
        SessaoDTO dto = new SessaoDTO();
        dto.setId(sessao.getId());
        dto.setTitulo(sessao.getTitulo());
        dto.setDescricao(sessao.getDescricao());
        dto.setTempoDefinido(sessao.getTempoDefinido());
        dto.setStatus(sessao.getStatus());
        dto.setCriadoEm(sessao.getCriadoEm());

        if (sessao.getProjeto() != null) {
            dto.setProjetoId(sessao.getProjeto().getId());
        }
        if (sessao.getTestador() != null) {
            dto.setTestadorId(sessao.getTestador().getId());
        }
        if (sessao.getEstrategia() != null) {
            dto.setEstrategiaId(sessao.getEstrategia().getId());
        }
        
        return dto;
    }

    public Sessao toEntity(SessaoCreateDTO createDto) {
        if (createDto == null) {
            return null;
        }
        Sessao sessao = new Sessao();
        sessao.setTitulo(createDto.getTitulo());
        sessao.setDescricao(createDto.getDescricao());
        sessao.setTempoDefinido(createDto.getTempoDefinido());
        return sessao;
    }


    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setLogin(usuario.getLogin());
        dto.setTipo(usuario.getTipo());
        return dto;
    }

    public Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNome(dto.getNome());
        usuario.setLogin(dto.getLogin());
        usuario.setTipo(dto.getTipo());
        // A senha deve ser tratada (codificada) e definida na camada de Service.
        return usuario;
    }
    
    // ======================================================
    // Mapeamento de BUG
    // ======================================================

    public BugDTO toDTO(Bug bug) {
        if (bug == null) {
            return null;
        }
        BugDTO dto = new BugDTO();
        dto.setId(bug.getId());
        dto.setDescricao(bug.getDescricao());
        dto.setSeveridade(bug.getSeveridade());
        dto.setTimestamp(bug.getTimestamp());
        
        if (bug.getSessao() != null) {
            dto.setSessaoId(bug.getSessao().getId());
        }
        
        return dto;
    }
    
    public Bug toEntity(BugDTO dto) {
        if (dto == null) {
            return null;
        }
        Bug bug = new Bug();
        bug.setId(dto.getId());
        bug.setDescricao(dto.getDescricao());
        bug.setSeveridade(dto.getSeveridade());
        // O timestamp é gerado pelo servidor (@PrePersist)
        // A entidade 'sessao' deve ser buscada e associada na camada de Service.
        return bug;
    }
}