package br.edu.fatec.tcc.guaranin.dto

import java.util.*

/**
 * DTO de resposta da API para [br.edu.fatec.tcc.guaranin.model.URLMonitorada].
 *
 * @property id identificador único da URL monitorada.
 * @property link endereço web monitorado.
 * @property ip endereço IP resolvido ou associado, quando aplicável.
 * @property padrao indica se é uma URL padrão da aplicação ou definida pelo usuário.
 */
data class URLMonitoradaResponseDTO(
    var id: UUID,
    var link: String,
    var ip: String?,
    var padrao: Boolean
) {}
