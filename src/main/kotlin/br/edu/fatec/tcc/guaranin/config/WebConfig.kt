package br.edu.fatec.tcc.guaranin.config

import br.edu.fatec.tcc.guaranin.interceptor.ApiKeyInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(val apiKeyInterceptor: ApiKeyInterceptor) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(apiKeyInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/**/public/**")
    }
}