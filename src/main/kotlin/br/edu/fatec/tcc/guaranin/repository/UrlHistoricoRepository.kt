package br.edu.fatec.tcc.guaranin.repository

import br.edu.fatec.tcc.guaranin.model.UrlHistorico
import br.edu.fatec.tcc.guaranin.model.UsuarioURL
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import java.awt.print.Pageable
import java.time.LocalDateTime
import java.util.*

/**
 * Repositório responsável pelo acesso e persistência das entidades [UrlHistorico].
 *
 * Estende [JpaRepository] para operações CRUD padrão e fornece consultas
 * personalizadas por associação [UsuarioURL] e data/hora de acesso.
 */
interface UrlHistoricoRepository : JpaRepository<UrlHistorico, UUID> {

    /**
     * Busca histórico de URL associado a uma relação [UsuarioURL].
     *
     * @param usuarioURL relação [UsuarioURL] critério de busca.
     * @return [Optional] contendo a entidade encontrada ou vazio.
     */
    fun findByIdUsuarioUrl(usuarioURL: UsuarioURL): Optional<UrlHistorico>

    /**
     * Busca histórico de URL pela data e hora exata do acesso.
     *
     * @param dataHoraAcesso data e hora do acesso.
     * @return [Optional] contendo a entidade encontrada ou vazio.
     */
    fun findByDataHoraAcesso(dataHoraAcesso: LocalDateTime, pageable: Pageable): Page<UrlHistorico>
}