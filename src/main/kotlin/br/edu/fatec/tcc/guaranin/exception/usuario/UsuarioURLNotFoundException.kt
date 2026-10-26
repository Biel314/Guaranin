package br.edu.fatec.tcc.guaranin.exception.usuario

import jakarta.persistence.EntityNotFoundException

/**
 * Exceção lançada quando uma associação
 * [br.edu.fatec.tcc.guaranin.model.UsuarioURL] não é encontrada no repositório.
 *
 * Herda de [EntityNotFoundException] e deve ser convertida em resposta HTTP
 * `404` pela camada de controle da API de associações.
 *
 * @property message mensagem descritiva da exceção.
 */
class UsuarioURLNotFoundException(
    message: String = "UsuarioURL not found",
) : EntityNotFoundException(message) {
}
