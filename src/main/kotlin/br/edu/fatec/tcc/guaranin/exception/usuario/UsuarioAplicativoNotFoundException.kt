package br.edu.fatec.tcc.guaranin.exception.usuario

import jakarta.persistence.EntityNotFoundException

class UsuarioAplicativoNotFoundException(
        message: String = "Usuario Aplicativo not found!"
    ) : EntityNotFoundException(message)
{
}