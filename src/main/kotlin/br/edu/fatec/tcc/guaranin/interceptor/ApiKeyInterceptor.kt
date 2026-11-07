package br.edu.fatec.tcc.guaranin.interceptor

import br.edu.fatec.tcc.guaranin.exception.APIKeyInvalidException
import br.edu.fatec.tcc.guaranin.exception.APIKeyNotProvidedException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class ApiKeyInterceptor : HandlerInterceptor {
    val publicPaths: List<String> = mutableListOf(
        "/public/**",
        "/auth/login",
        "/auth/register",
        "/api/v1/produtos"
    )

    val CHAVES_VALIDAS: Set<String> = mutableSetOf(
        "cco123",
        "azul123"
    )

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        var apiKey: String?

        try {
            apiKey = request.getHeader("X-API-KEY")
        } catch (e: Exception) {
            throw APIKeyNotProvidedException()
        }

        if (apiKey == null || !isValidApiKey(apiKey)) {
            throw APIKeyInvalidException()
        }

        return true
    }

    fun isValidApiKey(apiKey: String): Boolean {
        return CHAVES_VALIDAS.contains(apiKey)
    }
}