package br.edu.fatec.tcc.guaranin.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class DebugConfig(

    @Value("\${debug.enabled}")
    val enabled: Boolean,

    @Value("\${debug.admin-user-id:}")
    val adminUserId: String,
) {

}