package br.edu.fatec.tcc.guaranin.exception.usuario

import jakarta.persistence.EntityExistsException

class UsuarioAplicativoJaExisteException(
    message: String = "Associacao entre usuario e aplicativo ja existe no banco"
) : EntityExistsException(message) {
}