package br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado

import java.util.UUID

class AplicativoMonitoradoResponseDTO(
    val id: UUID,
    val pacote: String,
    val nome: String,
    val padrao: Boolean
    )
{}