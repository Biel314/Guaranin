package br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado

import java.time.LocalDateTime
import java.util.UUID

class AplicativoHistoricoCreateDTO(
    val usuarioAplicativoId: UUID,

    val dataHoraAcesso: LocalDateTime
)
{

}