package br.edu.fatec.tcc.guaranin.repository

import br.edu.fatec.tcc.guaranin.model.URLHistorico
import br.edu.fatec.tcc.guaranin.model.UsuarioURL
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

/**
 * Repositório responsável pelo acesso e persistência das entidades [URLHistorico].
 *
 * Estende [JpaRepository] para operações CRUD padrão e fornece consultas
 * personalizadas por associação [UsuarioURL] e data/hora de acesso.
 */
@Repository
interface URLHistoricoRepository : JpaRepository<URLHistorico, UUID> {

    /**
     * Busca histórico de URL associado a uma relação [UsuarioURL].
     *
     * @param usuarioURL relação [UsuarioURL] critério de busca.
     * @return [Optional] contendo a entidade encontrada ou vazio.
     */
    fun findByIdUsuarioUrl(usuarioURL: UsuarioURL): Optional<URLHistorico>

    /**
     * Busca histórico de URL pela data e hora do acesso com paginação.
     *
     * @param dataHoraAcesso data e hora do acesso.
     * @param pageable parâmetros de paginação.
     * @return [Page] contendo as entidades encontradas.
     */
    fun findByDataHoraAcesso(dataHoraAcesso: LocalDateTime, pageable: Pageable): Page<URLHistorico>
}
