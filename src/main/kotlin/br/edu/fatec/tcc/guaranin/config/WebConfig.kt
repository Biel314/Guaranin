package br.edu.fatec.tcc.guaranin.config

import br.edu.fatec.tcc.guaranin.interceptor.ApiKeyInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Configuração do Spring MVC da aplicação.
 *
 * Registra o [ApiKeyInterceptor] para validar as requisições da aplicação.
 *
 * @property apiKeyInterceptor interceptor responsável pela validação da API key.
 * @see ApiKeyInterceptor
 */
@Configuration
class WebConfig(val apiKeyInterceptor: ApiKeyInterceptor) : WebMvcConfigurer {

    /**
     * Registra o [ApiKeyInterceptor] e define os padrões de caminho interceptados.
     *
     * @param registry registro de interceptores do Spring MVC.
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(apiKeyInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/**/public/**")
    }
}
