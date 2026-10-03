package br.edu.fatec.tcc.guaranin.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import java.time.LocalDateTime
import java.util.*

/**
 * DTO de entrada para atualização de [br.edu.fatec.tcc.guaranin.model.URLHistorico].
 */
data class URLHistoricoUpdateDTO(

    @field:NotNull(message = "O ID é obrigatório")
    val id: UUID,

    val usuarioUrlId: UUID?,

    @field:PastOrPresent(message = "A data e hora do acesso devem estar no passado ou presente")
    val dataHoraAcesso: LocalDateTime?
) {}
