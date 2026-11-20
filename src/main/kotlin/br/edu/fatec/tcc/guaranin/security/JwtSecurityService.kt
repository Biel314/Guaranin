package br.edu.fatec.tcc.guaranin.security

import org.springframework.security.core.context.SecurityContextHolder
import java.nio.file.AccessDeniedException
import java.util.*

class JwtSecurityService : SecurityService {

    override fun getUsuarioLogadoId(): UUID {

        val authentication =
            SecurityContextHolder
                .getContext()
                .authentication
                ?: throw IllegalStateException(
                    "Usuário não autenticado"
                )

        return UUID.fromString(
            authentication.name
        )
    }

    @Throws(java.nio.file.AccessDeniedException::class)
    override fun validarUsuario(
        usuarioId: UUID?
    ): Boolean {
        if (
            usuarioId != null &&
            getUsuarioLogadoId()
            != usuarioId
        ) {
            throw AccessDeniedException(
                "Acesso negado"
            )
        }
        return true
    }
}