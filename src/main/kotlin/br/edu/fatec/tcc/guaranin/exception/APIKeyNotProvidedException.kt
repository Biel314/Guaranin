package br.edu.fatec.tcc.guaranin.exception

/**
 * Exceção lançada quando a requisição não informa o header `X-API-KEY`.
 *
 * Convertida em resposta HTTP `401 Unauthorized` pelo
 * [br.edu.fatec.tcc.guaranin.controller.GlobalAPIExceptionHandler].
 *
 * @property message mensagem descritiva do erro, com valor padrão
 * "API Key not provided".
 */
class APIKeyNotProvidedException(
    message: String = "API Key not provided"
) : APIException(message) {
}
