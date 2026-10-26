package br.edu.fatec.tcc.guaranin.dto.usuario

import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoMonitoradoResponseDTO
import java.util.UUID

class UsuarioAplicativoResponseDTO (
    val id: UUID,
    val aplicativoMonitoradoResponseDTO: AplicativoMonitoradoResponseDTO,
    val qtdAcesso: Int,
    val tempoAtivo: Int
) {
}