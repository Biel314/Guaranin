package br.edu.fatec.tcc.guaranin.exception

/**
 * Exceção lançada quando a API key informada não está entre as chaves válidas.
 *
 * Convertida em resposta HTTP `401 Unauthorized` pelo
 * [br.edu.fatec.tcc.guaranin.controller.GlobalAPIExceptionHandler].
 *
 * @property message mensagem descritiva do erro, com valor padrão
 * "Invalid API Key.".
 */
class APIKeyInvalidException(
    message: String = "Invalid API Key."
) : APIException(message) {
}
