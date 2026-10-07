package br.edu.fatec.tcc.guaranin.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import java.time.LocalDateTime
import java.util.*

/**
 * DTO de entrada para criação de [br.edu.fatec.tcc.guaranin.model.URLHistorico].
 */
data class URLHistoricoCreateDTO(

    @field:NotNull(message = "O ID da associação UsuarioURL é obrigatório")
    val usuarioUrlId: UUID,

    @field:NotNull(message = "A data e hora do acesso são obrigatórias")
    @field:PastOrPresent(message = "A data e hora do acesso devem estar no passado ou presente")
    val dataHoraAcesso: LocalDateTime = LocalDateTime.now()
) {}
