package br.edu.fatec.tcc.guaranin.exception.urlmonitorada

import jakarta.persistence.EntityNotFoundException

/**
 * Exceção lançada quando uma entidade [br.edu.fatec.tcc.guaranin.model.URLHistorico]
 * não é encontrada no repositório.
 *
 * Herda de [EntityNotFoundException] e é convertida em resposta HTTP `404`
 * pelo [br.edu.fatec.tcc.guaranin.controller.urlmonitorada.URLHistoricoAPIExceptionHandler].
 *
 * @property message mensagem descritiva do erro.
 */
class URLHistoricoNotFoundException(
    message: String = "URL histórico não encontrado",
) : EntityNotFoundException(message)