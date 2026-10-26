package br.edu.fatec.tcc.guaranin.exception.usuario

import jakarta.persistence.EntityNotFoundException

/**
 * Exceção lançada quando um [br.edu.fatec.tcc.guaranin.model.Usuario] não é
 * encontrado no repositório.
 *
 * Herda de [EntityNotFoundException] e é convertida em resposta HTTP `404`
 * pelo [br.edu.fatec.tcc.guaranin.controller.usuario.UsuarioAPIExceptionHandler],
 * que utiliza a mensagem "Usuário não encontrado".
 *
 * @property message mensagem descritiva da exceção, com valor padrão
 * "Usuario not found!".
 */
class UsuarioNotFoundException(
    message: String = "Usuario not found!"
) : EntityNotFoundException(message)