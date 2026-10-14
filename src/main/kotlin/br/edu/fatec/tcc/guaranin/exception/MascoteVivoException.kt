package br.edu.fatec.tcc.guaranin.exception

import jakarta.persistence.EntityExistsException

class MascoteVivoException(
    message: String = "Ja existe um mascote vivo..."
) : EntityExistsException(message) {
}