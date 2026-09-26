package br.edu.fatec.tcc.guaranin.exception

import jakarta.persistence.EntityNotFoundException

class MascoteNotFoundException (
    message: String = "Mascote not found!"
) : EntityNotFoundException(message) {
}