package br.edu.fatec.tcc.guaranin.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class PasswordConfig {

    @Bean
    fun encoder(): PasswordEncoder {
        return BCryptPasswordEncoder(12)
    }
}