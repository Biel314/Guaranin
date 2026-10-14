package br.edu.fatec.tcc.guaranin.exception

import jakarta.persistence.EntityExistsException

class MascoteVivoException(
    message: String = "Mascote not found!"
) : EntityExistsException(message) {
}