package br.edu.fatec.tcc.guaranin.exception

import jakarta.persistence.EntityNotFoundException

class UsuarioNotFoundException(
    message: String = "Usuario not found!"
) : EntityNotFoundException(message) {
}