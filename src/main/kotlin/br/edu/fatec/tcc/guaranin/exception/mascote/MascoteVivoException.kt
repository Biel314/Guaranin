package br.edu.fatec.tcc.guaranin.exception.mascote

import jakarta.persistence.EntityExistsException

class MascoteVivoException(
    message: String = "Ja existe um mascote vivo..."
) : EntityExistsException(message) {
}