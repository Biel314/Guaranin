package br.edu.fatec.tcc.guaranin.dto.urlmonitorada

import br.edu.fatec.tcc.guaranin.validation.urlmonitorada.ValidIp
import br.edu.fatec.tcc.guaranin.validation.urlmonitorada.ValidUrl
import jakarta.validation.constraints.Size

/**
 * DTO de entrada para atualização de [br.edu.fatec.tcc.guaranin.model.URLMonitorada].
 *
 * Campos opcionais: valores `null` são ignorados pelo mapeamento, preservando
 * o estado atual da entidade.
 *
 * O identificador é recebido pelo path da requisição, não pelo DTO.
 *
 * @property link novo endereço web opcional.
 * @property ip novo endereço IP opcional.
 * @property padrao novo status de marcação padrão opcional.
 */
data class URLMonitoradaUpdateDTO(
    @field:Size(max = 4050, message = "O link deve ter no máximo 4050 caracteres")
    @field:ValidUrl
    val link: String? = null,

    @field:Size(max = 50, message = "O IP deve ter no máximo 50 caracteres")
    @field:ValidIp
    val ip: String? = null,

    val padrao: Boolean? = null
) {}
