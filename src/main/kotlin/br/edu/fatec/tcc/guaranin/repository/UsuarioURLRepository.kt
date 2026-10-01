package br.edu.fatec.tcc.guaranin.repository

import br.edu.fatec.tcc.guaranin.model.URLMonitorada
import br.edu.fatec.tcc.guaranin.model.Usuario
import br.edu.fatec.tcc.guaranin.model.UsuarioURL
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UsuarioURLRepository : JpaRepository<UsuarioURL, UUID> {
    fun findByUsuario(usuario: Usuario, pageable: Pageable): Page<UsuarioURL>
    fun findByUrlMonitorada(urlMonitorada: URLMonitorada, pageable: Pageable): Page<UsuarioURL>
}