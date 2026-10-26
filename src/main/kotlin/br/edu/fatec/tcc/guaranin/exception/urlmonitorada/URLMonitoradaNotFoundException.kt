package br.edu.fatec.tcc.guaranin.exception.urlmonitorada

import jakarta.persistence.EntityNotFoundException

/**
 * Exceção lançada quando uma entidade [br.edu.fatec.tcc.guaranin.model.URLMonitorada]
 * não é encontrada no repositório.
 *
 * Herda de [EntityNotFoundException] e é convertida em resposta HTTP pelo
 * [br.edu.fatec.tcc.guaranin.controller.urlmonitorada.URLMonitoradaAPIExceptionHandler].
 *
 * @property message mensagem descritiva do erro.
 */
class URLMonitoradaNotFoundException(
    message: String = "URL monitorada não encontrada",
) : EntityNotFoundException(message) {
}