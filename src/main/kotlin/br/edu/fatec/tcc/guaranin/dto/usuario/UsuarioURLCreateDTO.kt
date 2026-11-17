package br.edu.fatec.tcc.guaranin.dto.usuario

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.util.*

/**
 * DTO de entrada para criação de
 * [br.edu.fatec.tcc.guaranin.model.UsuarioURL].
 *
 * Os identificadores são resolvidos para entidades pelo service antes da
 * persistência. Quantidade de acessos e tempo ativo não podem ser negativos.
 *
 * @property urlMonitoradaId identificador da URL monitorada associada;
 * obrigatório.
 * @property qtdAcesso quantidade de acessos; deve ser maior ou igual a zero.
 * @property tempoAtivo tempo ativo em segundos; deve ser maior ou igual a zero.
 */
data class UsuarioURLCreateDTO(
    @field:NotNull(message = "O ID da URL monitorada é obrigatório")
    val urlMonitoradaId: UUID,

    @field:NotNull(message = "A quantidade de acesso é obrigatória")
    @field:Min(value = 0, message = "A quantidade de acesso deve ser maior ou igual a zero")
    val qtdAcesso: Short = 0,

    @field:NotNull(message = "O tempo ativo é obrigatório")
    @field:Min(value = 0, message = "O tempo ativo deve ser maior ou igual a zero")
    val tempoAtivo: Int = 0
)
