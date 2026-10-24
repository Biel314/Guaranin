package br.edu.fatec.tcc.guaranin.dto.urlmonitorada

import br.edu.fatec.tcc.guaranin.validation.urlmonitorada.ValidIp
import br.edu.fatec.tcc.guaranin.validation.urlmonitorada.ValidUrl
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * DTO de entrada para criação de [br.edu.fatec.tcc.guaranin.model.URLMonitorada].
 *
 * O identificador é omitido por ser gerado pela camada de persistência.
 *
 * @property link endereço web a ser monitorado (obrigatório e validado).
 * @property ip endereço IP associado opcional.
 * @property padrao indica se a URL é padrão da aplicação (padrão `false`).
 */
data class URLMonitoradaCreateDTO(

    @field:NotBlank(message = "O link é obrigatório")
    @field:Size(max = 4050, message = "O link deve ter no máximo 4050 caracteres")
    @field:ValidUrl
    val link: String,

    @field:Size(max = 50, message = "O IP deve ter no máximo 50 caracteres")
    @field:ValidIp
    val ip: String? = null,

    val padrao: Boolean = false
) {}
