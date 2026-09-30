package br.edu.fatec.tcc.guaranin.dto

import br.edu.fatec.tcc.guaranin.validation.ValidIp
import br.edu.fatec.tcc.guaranin.validation.ValidUrl
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * DTO de entrada para criação de [br.edu.fatec.tcc.guaranin.model.URLMonitorada].
 *
 * O identificador é omitido por ser gerado pela camada de persistência.
 */
data class URLMonitoradaCreateDTO(

    @field:NotBlank(message = "O link é obrigatório")
    @field:ValidUrl
    val link: String,

    @field:Size(max = 50, message = "O IP deve ter no máximo 50 caracteres")
    @field:ValidIp
    val ip: String?,

    val padrao: Boolean = false
) {}
