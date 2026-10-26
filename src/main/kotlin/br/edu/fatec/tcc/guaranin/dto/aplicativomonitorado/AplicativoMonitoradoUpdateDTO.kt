package br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado

import java.util.UUID

/**
 * DTO de entrada utilizado para atualização de entidades
 * [AplicativoMonitorado].
 *
 * Contém os dados que podem ser alterados após a criação do
 * aplicativo monitorado.
 *
 * @property id identificador único do aplicativo monitorado.
 * @property pacote nome do pacote atualizado.
 * @property nome nome atualizado do aplicativo.
 */
class AplicativoMonitoradoUpdateDTO(

    val id: UUID,

    val pacote: String,

    val nome: String
)
{
}