package br.edu.fatec.tcc.guaranin.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig(

    @Value("\${jwt.secret}")
    val secret: String,

    @Value("\${jwt.expiration}")
    val expiration: Long
)