package br.edu.fatec.tcc.guaranin.dto

import java.util.*

/**
 * DTO de resposta da API para [br.edu.fatec.tcc.guaranin.model.URLMonitorada].
 */
data class URLMonitoradaResponseDTO(
    var id: UUID,
    var link: String,
    var ip: String?,
    var padrao: Boolean
) {}
