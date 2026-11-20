package br.edu.fatec.tcc.guaranin.config

import br.edu.fatec.tcc.guaranin.security.JwtSecurityService
import br.edu.fatec.tcc.guaranin.security.SecurityService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SecurityServiceConfig {
    @Bean
    fun securityService(): SecurityService {
        return JwtSecurityService()
    }
}