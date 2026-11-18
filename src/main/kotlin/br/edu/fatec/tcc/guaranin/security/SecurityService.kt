package br.edu.fatec.tcc.guaranin.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SecurityService {

    fun getUsuarioLogadoId(): UUID {

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
}
