package br.edu.fatec.tcc.guaranin.exception

/**
 * Exceção base para falhas de autenticação da API.
 *
 * Subclasses representam ausência ou invalidade da API key e são convertidas
 * em resposta HTTP `401 Unauthorized` pelo
 * [br.edu.fatec.tcc.guaranin.controller.GlobalAPIExceptionHandler].
 *
 * @property message mensagem descritiva do erro.
 * @see APIKeyNotProvidedException
 * @see APIKeyInvalidException
 */
open class APIException(message: String) : RuntimeException(message) {
}
