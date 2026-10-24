package br.edu.fatec.tcc.guaranin.dto.usuario

import jakarta.validation.constraints.Min
import java.util.*

/**
 * DTO de entrada para atualização parcial de
 * [br.edu.fatec.tcc.guaranin.model.UsuarioURL].
 *
 * O identificador do registro é informado no caminho da requisição. Campos
 * nulos preservam os valores atuais; as associações são resolvidas pelo
 * service quando seus identificadores são informados.
 *
 * @property usuarioId novo identificador do usuário; opcional.
 * @property urlMonitoradaId novo identificador da URL monitorada; opcional.
 * @property qtdAcesso nova quantidade de acessos; opcional e não negativa.
 * @property tempoAtivo novo tempo ativo em segundos; opcional e não negativo.
 */
data class UsuarioURLUpdateDTO(
    val usuarioId: UUID?,

    val urlMonitoradaId: UUID?,

    @field:Min(value = 0, message = "A quantidade de acesso deve ser maior ou igual a zero")
    val qtdAcesso: Short?,

    @field:Min(value = 0, message = "O tempo ativo deve ser maior ou igual a zero")
    val tempoAtivo: Int?
)
