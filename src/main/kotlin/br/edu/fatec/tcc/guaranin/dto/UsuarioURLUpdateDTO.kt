package br.edu.fatec.tcc.guaranin.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.util.*

/**
 * DTO de entrada para atualização de [br.edu.fatec.tcc.guaranin.model.UsuarioURL].
 */
data class UsuarioURLUpdateDTO(

    @field:NotNull(message = "O ID é obrigatório")
    val id: UUID,

    val usuarioId: UUID?,

    val urlMonitoradaId: UUID?,

    @field:Min(value = 0, message = "A quantidade de acesso deve ser maior ou igual a zero")
    val qtdAcesso: Short?,

    @field:Min(value = 0, message = "O tempo ativo deve ser maior ou igual a zero")
    val tempoAtivo: Int?
) {}
