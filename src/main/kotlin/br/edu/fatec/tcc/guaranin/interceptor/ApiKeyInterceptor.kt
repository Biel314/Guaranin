package br.edu.fatec.tcc.guaranin.interceptor

import br.edu.fatec.tcc.guaranin.exception.APIKeyInvalidException
import br.edu.fatec.tcc.guaranin.exception.APIKeyNotProvidedException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.data.redis.core.StringRedisTemplate



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
class ApiKeyInterceptor (private var redisTemplate: StringRedisTemplate
) : HandlerInterceptor {

    private val REDIS_KEY_PREFIX: String = "api:key:"
    private val HEADER_NAME: String = "X-API-KEY"

    fun ApiKeyInterceptor(redisTemplate: StringRedisTemplate) {
        this.redisTemplate = redisTemplate
    }

    @Throws(Exception::class)
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val apiKey = request.getHeader(HEADER_NAME)

        if (apiKey == null || apiKey.isBlank()) {
            throw APIKeyNotProvidedException()
        }

        if (!isValidApiKey(apiKey)) {
            throw APIKeyInvalidException()
        }

        return true
    }

    private fun isValidApiKey(apiKey: String?): Boolean {
        //System.out.println("Chave= "+ redisTemplate.opsForValue().get("api:key"));
        val existe: Boolean? = redisTemplate.hasKey(REDIS_KEY_PREFIX + apiKey)
        return java.lang.Boolean.TRUE == existe
    }
}