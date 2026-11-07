package br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado

import java.util.UUID

/**
 * DTO de saída utilizado para representar entidades
 * [AplicativoMonitorado].
 *
 * Contém os dados públicos do aplicativo monitorado que podem ser
 * expostos pela API.
 *
 * @property id identificador único do aplicativo monitorado.
 * @property pacote nome do pacote do aplicativo.
 * @property nome nome amigável do aplicativo.
 * @property padrao indica se o aplicativo é considerado padrão pelo
 * sistema.
 */
class AplicativoMonitoradoResponseDTO(

    val id: UUID,

    val pacote: String,

    val nome: String,

    val padrao: Boolean
)
{

}