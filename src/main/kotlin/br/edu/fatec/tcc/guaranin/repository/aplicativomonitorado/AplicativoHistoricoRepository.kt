package br.edu.fatec.tcc.guaranin.repository.aplicativomonitorado

import br.edu.fatec.tcc.guaranin.model.AplicativoHistorico
import br.edu.fatec.tcc.guaranin.model.Usuario
import br.edu.fatec.tcc.guaranin.model.UsuarioAplicativo
import java.util.UUID
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Repositório responsável pelo acesso e persistência das entidades [AplicativoHistorico].
 *
 * Estende [JpaRepository] para operações CRUD padrão e disponibiliza
 * consultas derivadas para recuperação de históricos de acesso a
 * aplicativos por associação [UsuarioAplicativo] e por [Usuario].
 *
 * As consultas utilizam os relacionamentos da entidade para navegar
 * entre histórico, associação usuário-aplicativo e usuário.
 *
 * @see AplicativoHistorico
 * @see UsuarioAplicativo
 * @see Usuario
 */
interface AplicativoHistoricoRepository : JpaRepository<AplicativoHistorico, UUID> {

    /**
     * Busca históricos associados a um registro de [UsuarioAplicativo]
     * utilizando paginação.
     *
     * @param usuarioAplicativoId identificador da associação
     * usuário-aplicativo.
     * @param pageable parâmetros de paginação e ordenação.
     * @return [Page] contendo os históricos encontrados.
     */
    fun findAllByUsuarioAplicativoId(
        usuarioAplicativoId: UUID,
        pageable: Pageable
    ): Page<AplicativoHistorico>

    /**
     * Busca todos os históricos associados a um registro de
     * [UsuarioAplicativo].
     *
     * @param usuarioAplicativo associação usuário-aplicativo utilizada
     * como critério de busca.
     * @param pageable parâmetros de paginação e ordenação.
     * @return [Page] contendo os históricos encontrados.
     */
    fun findAllByUsuarioAplicativo(
        usuarioAplicativo: UsuarioAplicativo,
        pageable: Pageable
    ): Page<AplicativoHistorico>

    /**
     * Busca todos os históricos associados ao identificador de uma
     * relação [UsuarioAplicativo].
     *
     * @param usuarioAplicativoId identificador da associação
     * usuário-aplicativo.
     * @return lista contendo todos os históricos encontrados.
     */
    fun findAllByUsuarioAplicativoId(
        usuarioAplicativoId: UUID
    ): List<AplicativoHistorico>

    /**
     * Busca históricos de aplicativos pertencentes a um usuário
     * específico utilizando paginação.
     *
     * A consulta navega pelo relacionamento:
     * AplicativoHistorico -> UsuarioAplicativo -> Usuario.
     *
     * @param usuarioId identificador único do usuário.
     * @param pageable parâmetros de paginação e ordenação.
     * @return [Page] contendo os históricos encontrados.
     */
    fun findAllByUsuarioAplicativoUsuarioId(
        usuarioId: UUID,
        pageable: Pageable
    ): Page<AplicativoHistorico>

    /**
     * Busca históricos de aplicativos pertencentes ao usuário informado
     * utilizando paginação.
     *
     * A consulta navega pelo relacionamento:
     * AplicativoHistorico -> UsuarioAplicativo -> Usuario.
     *
     * @param usuario usuário utilizado como critério de busca.
     * @param pageable parâmetros de paginação e ordenação.
     * @return [Page] contendo os históricos encontrados.
     */
    fun findAllByUsuarioAplicativoUsuario(
        usuario: Usuario,
        pageable: Pageable
    ): Page<AplicativoHistorico>
}