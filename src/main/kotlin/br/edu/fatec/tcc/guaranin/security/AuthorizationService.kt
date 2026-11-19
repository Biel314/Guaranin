package br.edu.fatec.tcc.guaranin.security

import br.edu.fatec.tcc.guaranin.model.Mascote
import org.springframework.stereotype.Service
import java.nio.file.AccessDeniedException
import java.util.UUID

@Service
class AuthorizationService(
    private val securityService: SecurityService
) {

    @Throws(AccessDeniedException::class)
    fun validarUsuario(
        usuarioId: UUID
    ) {

        if (
            securityService.getUsuarioLogadoId()
            != usuarioId
        ) {
            throw AccessDeniedException(
                "Acesso negado"
            )
        }
    }

    fun getUsuarioLogadoId(): UUID {
        return securityService.getUsuarioLogadoId()
    }
}