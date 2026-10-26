package br.edu.fatec.tcc.guaranin.dto.urlmonitorada

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import java.time.LocalDateTime
import java.util.*

/**
 * DTO de entrada para criação de [br.edu.fatec.tcc.guaranin.model.URLHistorico].
 *
 * Todos os campos são validados pelo Bean Validation antes de chegarem ao
 * serviço. Quando `dataHoraAcesso` é omitida, assume o instante atual.
 *
 * @property usuarioUrlId identificador da associação
 * [br.edu.fatec.tcc.guaranin.model.UsuarioURL]; obrigatório.
 * @property dataHoraAcesso data e hora do acesso; obrigatória e não pode
 * estar no futuro.
 */
data class URLHistoricoCreateDTO(

    @field:NotNull(message = "O ID da associação UsuarioURL é obrigatório")
    val usuarioUrlId: UUID,

    @field:NotNull(message = "A data e hora do acesso são obrigatórias")
    @field:PastOrPresent(message = "A data e hora do acesso devem estar no passado ou presente")
    val dataHoraAcesso: LocalDateTime = LocalDateTime.now()
) {}
