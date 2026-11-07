package br.edu.fatec.tcc.guaranin.repository.urlmonitorada

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
 * personalizadas por associação [UsuarioURL], usuário e data/hora de acesso.
 * As consultas derivadas por usuário atravessam a associação
 * `usuarioUrl.usuario` da entidade.
 *
 * @see URLHistorico
 * @see UsuarioURL
 */
@Repository
interface URLHistoricoRepository : JpaRepository<URLHistorico, UUID> {

    /**
     * Busca o histórico associado ao identificador de uma relação [UsuarioURL].
     *
     * @param usuarioUrlId identificador da associação [UsuarioURL] usada como
     * critério de busca.
     * @return [Optional] contendo a entidade encontrada ou vazio quando não
     * existe histórico para a associação informada.
     */
    fun findByUsuarioUrlId(usuarioUrlId: UUID): Optional<URLHistorico>

    /**
     * Busca histórico de URL pela data e hora do acesso com paginação.
     *
     * @param dataHoraAcesso data e hora do acesso.
     * @param pageable parâmetros de paginação.
     * @return [Page] contendo as entidades encontradas.
     */
    fun findByDataHoraAcesso(dataHoraAcesso: LocalDateTime, pageable: Pageable): Page<URLHistorico>

    /**
     * Busca históricos de acesso pertencentes a um usuário, com paginação.
     *
     * A consulta é derivada a partir da associação `usuarioUrl.usuario`,
     * permitindo localizar todos os acessos registrados para o usuário.
     *
     * @param usuarioId identificador único do usuário.
     * @param pageable parâmetros de paginação e ordenação.
     * @return [Page] contendo os históricos encontrados.
     */
    fun findByUsuarioUrlUsuarioId(usuarioId: UUID, pageable: Pageable): Page<URLHistorico>
}
