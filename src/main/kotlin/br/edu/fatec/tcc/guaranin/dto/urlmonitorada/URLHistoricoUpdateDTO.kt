package br.edu.fatec.tcc.guaranin.dto.urlmonitorada

import jakarta.validation.constraints.PastOrPresent
import java.time.LocalDateTime
import java.util.*

/**
 * DTO de entrada para atualização parcial de
 * [br.edu.fatec.tcc.guaranin.model.URLHistorico].
 *
 * Campos nulos preservam os valores atuais da entidade. Quando informada,
 * `dataHoraAcesso` não pode estar no futuro.
 *
 * @property usuarioUrlId novo identificador da associação
 * [br.edu.fatec.tcc.guaranin.model.UsuarioURL]; opcional.
 * @property dataHoraAcesso nova data e hora do acesso; opcional e não pode
 * estar no futuro.
 */
data class URLHistoricoUpdateDTO(
    val usuarioUrlId: UUID?,

    @field:PastOrPresent(message = "A data e hora do acesso devem estar no passado ou presente")
    val dataHoraAcesso: LocalDateTime?
) {}
