package br.edu.fatec.tcc.guaranin.repository.usuario

import br.edu.fatec.tcc.guaranin.model.AplicativoMonitorado
import br.edu.fatec.tcc.guaranin.model.Usuario
import br.edu.fatec.tcc.guaranin.model.UsuarioAplicativo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

/**
 * Repositório responsável pelo acesso e persistência das entidades
 * [UsuarioAplicativo].
 *
 * Estende [JpaRepository] para operações CRUD padrão e fornece consultas
 * derivadas por [Usuario] e [AplicativoMonitorado], permitindo recuperar
 * as associações entre usuários e aplicativos monitorados.
 *
 * As consultas podem ser realizadas por identificador da entidade ou
 * pela própria entidade associada, com ou sem paginação.
 *
 * @see UsuarioAplicativo
 * @see Usuario
 * @see AplicativoMonitorado
 */
interface UsuarioAplicativoRepository : JpaRepository<UsuarioAplicativo, UUID> {

    /**
     * Busca todas as associações pertencentes a um usuário.
     *
     * @param usuarioId identificador único do usuário.
     * @return lista contendo as associações encontradas.
     */
    fun findAllByUsuarioId(
        usuarioId: UUID
    ): List<UsuarioAplicativo>

    /**
     * Busca todas as associações pertencentes a um usuário com paginação.
     *
     * @param usuarioId identificador único do usuário.
     * @param pageable parâmetros de paginação e ordenação.
     * @return [Page] contendo as associações encontradas.
     */
    fun findAllByUsuarioId(
        usuarioId: UUID,
        pageable: Pageable
    ): Page<UsuarioAplicativo>

    /**
     * Busca todas as associações pertencentes ao usuário informado.
     *
     * @param usuario usuário utilizado como critério de busca.
     * @return lista contendo as associações encontradas.
     */
    fun findAllByUsuario(
        usuario: Usuario
    ): List<UsuarioAplicativo>

    /**
     * Busca todas as associações pertencentes ao usuário informado com
     * paginação.
     *
     * @param usuario usuário utilizado como critério de busca.
     * @param pageable parâmetros de paginação e ordenação.
     * @return [Page] contendo as associações encontradas.
     */
    fun findAllByUsuario(
        usuario: Usuario,
        pageable: Pageable
    ): Page<UsuarioAplicativo>

    /**
     * Busca todas as associações relacionadas ao aplicativo monitorado
     * informado por identificador.
     *
     * @param aplicativoMonitoradoId identificador único do aplicativo
     * monitorado.
     * @return lista contendo as associações encontradas.
     */
    fun findAllByAplicativoMonitoradoId(
        aplicativoMonitoradoId: UUID
    ): List<UsuarioAplicativo>

    /**
     * Busca todas as associações relacionadas ao aplicativo monitorado
     * informado por identificador utilizando paginação.
     *
     * @param aplicativoMonitoradoId identificador único do aplicativo
     * monitorado.
     * @param pageable parâmetros de paginação e ordenação.
     * @return [Page] contendo as associações encontradas.
     */
    fun findAllByAplicativoMonitoradoId(
        aplicativoMonitoradoId: UUID,
        pageable: Pageable
    ): Page<UsuarioAplicativo>

    /**
     * Busca todas as associações relacionadas ao aplicativo monitorado
     * informado.
     *
     * @param aplicativoMonitorado aplicativo monitorado utilizado como
     * critério de busca.
     * @return lista contendo as associações encontradas.
     */
    fun findAllByAplicativoMonitorado(
        aplicativoMonitorado: AplicativoMonitorado
    ): List<UsuarioAplicativo>

    /**
     * Busca todas as associações relacionadas ao aplicativo monitorado
     * informado utilizando paginação.
     *
     * @param aplicativoMonitorado aplicativo monitorado utilizado como
     * critério de busca.
     * @param pageable parâmetros de paginação e ordenação.
     * @return [Page] contendo as associações encontradas.
     */
    fun findAllByAplicativoMonitorado(
        aplicativoMonitorado: AplicativoMonitorado,
        pageable: Pageable
    ): Page<UsuarioAplicativo>
}