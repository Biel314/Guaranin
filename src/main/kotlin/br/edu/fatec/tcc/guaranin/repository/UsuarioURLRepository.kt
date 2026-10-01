package br.edu.fatec.tcc.guaranin.repository

import br.edu.fatec.tcc.guaranin.model.URLMonitorada
import br.edu.fatec.tcc.guaranin.model.Usuario
import br.edu.fatec.tcc.guaranin.model.UsuarioURL
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repositório responsável pelo acesso e persistência das entidades [UsuarioURL].
 */
@Repository
interface UsuarioURLRepository : JpaRepository<UsuarioURL, UUID> {

    /**
     * Busca associações de URL por usuário com paginação.
     *
     * @param usuario usuário critério de busca.
     * @param pageable parâmetros de paginação.
     * @return [Page] contendo as associações encontradas.
     */
    fun findByUsuario(usuario: Usuario, pageable: Pageable): Page<UsuarioURL>

    /**
     * Busca associações de URL por URL monitorada com paginação.
     *
     * @param urlMonitorada URL monitorada critério de busca.
     * @param pageable parâmetros de paginação.
     * @return [Page] contendo as associações encontradas.
     */
    fun findByUrlMonitorada(urlMonitorada: URLMonitorada, pageable: Pageable): Page<UsuarioURL>

    /**
     * Busca associações de URL por usuário e tempo ativo mínimo com paginação.
     *
     * @param usuario usuário critério de busca.
     * @param tempoAtivo tempo ativo mínimo.
     * @param pageable parâmetros de paginação.
     * @return [Page] contendo as associações encontradas.
     */
    fun findByUsuarioAndTempoAtivoGreaterThanEqual(usuario: Usuario, tempoAtivo: Int, pageable: Pageable): Page<UsuarioURL>
}