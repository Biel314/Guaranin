package br.edu.fatec.tcc.guaranin.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val authHeader = request.getHeader("Authorization")

        if (
            authHeader != null &&
            authHeader.startsWith("Bearer ")
        ) {

            val token = authHeader.substring(7)

            val usuarioId =
                jwtService.validarToken(token)

            if (usuarioId != null) {

                val authentication =
                    UsernamePasswordAuthenticationToken(
                        usuarioId,
                        null,
                        emptyList()
                    )

                SecurityContextHolder
                    .getContext()
                    .authentication = authentication
            }

            println("TOKEN: $token")
            println("USUARIO ID: $usuarioId")
        }

        filterChain.doFilter(
            request,
            response
        )
    }
}