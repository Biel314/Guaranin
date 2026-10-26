package br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado

/**
 * DTO de entrada utilizado para criação de entidades
 * [AplicativoMonitorado].
 *
 * Contém as informações necessárias para cadastrar um novo aplicativo
 * monitorado no sistema.
 *
 * @property pacote nome do pacote do aplicativo.
 * @property nome nome amigável do aplicativo.
 */
class AplicativoMonitoradoCreateDTO(

    val pacote: String,

    val nome: String
)