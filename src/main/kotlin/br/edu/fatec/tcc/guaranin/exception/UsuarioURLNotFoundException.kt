package br.edu.fatec.tcc.guaranin.exception

import jakarta.persistence.EntityNotFoundException

class UsuarioURLNotFoundException(
    message: String = "UsuarioURL not found",
) : EntityNotFoundException(message) {
}
