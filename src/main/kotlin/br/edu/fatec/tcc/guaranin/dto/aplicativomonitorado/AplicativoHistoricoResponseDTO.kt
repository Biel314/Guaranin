package br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado

import java.time.LocalDateTime
import java.util.UUID

data class AplicativoHistoricoResponseDTO(

    val id: UUID,

    val usuarioAplicativoId: UUID,

    val dataHoraAcesso: LocalDateTime
)