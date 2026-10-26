package br.edu.fatec.tcc.guaranin.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * Envelope padrão de erro da API.
 *
 * Usado pelo [br.edu.fatec.tcc.guaranin.controller.GlobalAPIExceptionHandler] e
 * pelos handlers de domínio das APIs para respostas no formato
 * `{"error": "..."}`. Mantém o contrato estável para consumidores da API.
 *
 * Exemplo:
 * ```json
 * {"error": "Usuário não encontrado"}
 * ```
 *
 * @property error mensagem legível sobre o erro ocorrido.
 * @see br.edu.fatec.tcc.guaranin.controller.GlobalAPIExceptionHandler
 * @see br.edu.fatec.tcc.guaranin.controller.usuario.UsuarioAPIExceptionHandler
 * @see br.edu.fatec.tcc.guaranin.controller.urlmonitorada.URLMonitoradaAPIExceptionHandler
 */
@Schema(description = "Erro retornado pela API")
data class ApiErrorDTO(
    @field:Schema(
        description = "Mensagem de erro",
        example = "Usuário não encontrado"
    )
    val error: String
)
