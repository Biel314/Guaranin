package br.edu.fatec.tcc.guaranin.dto

import br.edu.fatec.tcc.guaranin.validation.ValidIp
import br.edu.fatec.tcc.guaranin.validation.ValidUrl
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.*

/**
 * DTO de entrada para atualização de [br.edu.fatec.tcc.guaranin.model.URLMonitorada].
 *
 * Campos opcionais: valores `null` são ignorados pelo mapeamento, preservando
 * o estado atual da entidade.
 *
 * @property id identificador único obrigatório da URL a ser atualizada.
 * @property link novo endereço web opcional.
 * @property ip novo endereço IP opcional.
 * @property padrao novo status de marcação padrão opcional.
 */
data class URLMonitoradaUpdateDTO(

    @field:NotNull
    val id: UUID?,

    @field:ValidUrl
    val link: String?,

    @field:Size(max = 50, message = "O IP deve ter no máximo 50 caracteres")
    @field:ValidIp
    val ip: String?,

    val padrao: Boolean?
) {}
