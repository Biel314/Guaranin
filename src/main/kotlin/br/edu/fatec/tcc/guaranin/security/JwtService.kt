package br.edu.fatec.tcc.guaranin.security

import br.edu.fatec.tcc.guaranin.config.JwtConfig
import br.edu.fatec.tcc.guaranin.model.Usuario
import org.springframework.stereotype.Service
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.time.Instant

@Service
class JwtService(
    private val jwtConfig: JwtConfig
) {
    fun gerarToken(usuario: Usuario): String {
        return JWT.create()
            .withSubject(usuario.id.toString())
            .withClaim("email", usuario.email)
            .withExpiresAt(
                Instant.now().plusSeconds(3600)
            )
            .sign(
                Algorithm.HMAC256(jwtConfig.secret)
            )
    }

    fun validarToken(token: String): String? {
        return try {

            val decodedJWT =
                JWT.require(
                    Algorithm.HMAC256(jwtConfig.secret)
                )
                    .build()
                    .verify(token)

            println("SUBJECT = ${decodedJWT.subject}")
            println("EMAIL = ${decodedJWT.getClaim("email").asString()}")

            decodedJWT.subject

        } catch (e: Exception) {

            e.printStackTrace()

            null
        }
    }
}