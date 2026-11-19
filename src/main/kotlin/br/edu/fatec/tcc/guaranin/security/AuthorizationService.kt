package br.edu.fatec.tcc.guaranin.security

import org.springframework.stereotype.Service
import java.nio.file.AccessDeniedException
import java.util.*

@Service
class AuthorizationService(
    private val securityService: SecurityService
) {

    @Throws(AccessDeniedException::class)
    fun validarUsuario(
        usuarioId: UUID?
    ) {

        if (
            usuarioId != null &&
            securityService.getUsuarioLogadoId()
            != usuarioId
        ) {
            throw AccessDeniedException(
                "Acesso negado"
            )
        }
    }

    fun getIdUsuarioAutenticado(): UUID {
        return securityService.getUsuarioLogadoId()
    }
}