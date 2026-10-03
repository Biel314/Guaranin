package br.edu.fatec.tcc.guaranin.dto

import java.time.LocalDateTime
import java.util.*

/**
 * DTO de resposta da API para [br.edu.fatec.tcc.guaranin.model.URLHistorico].
 */
data class URLHistoricoResponseDTO(
    var id: UUID,
    var usuarioUrlId: UUID,
    var dataHoraAcesso: LocalDateTime
) {}
