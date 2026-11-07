package br.edu.fatec.tcc.guaranin.interceptor

import br.edu.fatec.tcc.guaranin.exception.APIKeyInvalidException
import br.edu.fatec.tcc.guaranin.exception.APIKeyNotProvidedException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

/**
 * Interceptor responsável por validar o header `X-API-KEY` das requisições.
 *
 * Registrado pelo [br.edu.fatec.tcc.guaranin.config.WebConfig] para validar
 * as requisições da API. Requisições sem o header são rejeitadas com
 * [APIKeyNotProvidedException]; chaves não reconhecidas são rejeitadas com
 * [APIKeyInvalidException]. Ambas são convertidas em `401 Unauthorized` pelo
 * [br.edu.fatec.tcc.guaranin.controller.GlobalAPIExceptionHandler].
 *
 * @see br.edu.fatec.tcc.guaranin.config.WebConfig
 * @see br.edu.fatec.tcc.guaranin.exception.APIException
 */
@Component
class ApiKeyInterceptor : HandlerInterceptor {
    /** Caminhos públicos previstos para dispensa da validação da API key. */
    val publicPaths: List<String> = mutableListOf(
        "/public/**",
        "/auth/login",
        "/auth/register",
        "/api/v1/produtos"
    )

    /** Chaves de API aceitas pelo interceptor. */
    val CHAVES_VALIDAS: Set<String> = mutableSetOf(
        "cco123",
        "azul123"
    )

    /**
     * Valida a API key antes da execução do handler.
     *
     * @param request requisição HTTP recebida.
     * @param response resposta HTTP, não utilizada neste interceptor.
     * @param handler handler que processará a requisição.
     * @return `true` quando a API key é válida.
     * @throws APIKeyNotProvidedException se o header `X-API-KEY` estiver ausente.
     * @throws APIKeyInvalidException se a API key informada não for válida.
     */
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        var apiKey: String? = request.getHeader("X-API-KEY")

        if (apiKey == null)
            throw APIKeyNotProvidedException()

        if (!isValidApiKey(apiKey))
            throw APIKeyInvalidException()

        return true
    }

    /**
     * Verifica se a API key informada está entre as chaves válidas.
     *
     * @param apiKey valor recebido no header `X-API-KEY`.
     * @return `true` se a chave for válida; `false` caso contrário.
     */
    fun isValidApiKey(apiKey: String): Boolean {
        return CHAVES_VALIDAS.contains(apiKey)
    }
}