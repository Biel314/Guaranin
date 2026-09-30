package br.edu.fatec.tcc.guaranin.exception

import jakarta.persistence.EntityNotFoundException

/**
 * Exceção de entidade [Usuario] não encontrada.
 */
class UsuarioNotFoundException(
    message: String = "Usuario not found!"
) : EntityNotFoundException(message) {
}