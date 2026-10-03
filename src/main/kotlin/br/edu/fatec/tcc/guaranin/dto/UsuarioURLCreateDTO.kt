package br.edu.fatec.tcc.guaranin.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.util.*

/**
 * DTO de entrada para criação de [br.edu.fatec.tcc.guaranin.model.UsuarioURL].
 */
data class UsuarioURLCreateDTO(

    @field:NotNull(message = "O ID do usuário é obrigatório")
    val usuarioId: UUID,

    @field:NotNull(message = "O ID da URL monitorada é obrigatório")
    val urlMonitoradaId: UUID,

    @field:NotNull(message = "A quantidade de acesso é obrigatória")
    @field:Min(value = 0, message = "A quantidade de acesso deve ser maior ou igual a zero")
    val qtdAcesso: Short = 0,

    @field:NotNull(message = "O tempo ativo é obrigatório")
    @field:Min(value = 0, message = "O tempo ativo deve ser maior ou igual a zero")
    val tempoAtivo: Int = 0
) {}
