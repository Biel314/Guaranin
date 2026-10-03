package br.edu.fatec.tcc.guaranin.dto

import java.util.*

/**
 * DTO de resposta da API para [br.edu.fatec.tcc.guaranin.model.UsuarioURL].
 */
data class UsuarioURLResponseDTO(
    var id: UUID,
    var usuarioId: UUID,
    var urlMonitoradaId: UUID,
    var qtdAcesso: Short,
    var tempoAtivo: Int
) {}
