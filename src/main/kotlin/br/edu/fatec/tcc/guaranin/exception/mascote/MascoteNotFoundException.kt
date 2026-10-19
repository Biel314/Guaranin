package br.edu.fatec.tcc.guaranin.exception.mascote

import jakarta.persistence.EntityNotFoundException

class MascoteNotFoundException (
    message: String = "Mascote not found!"
) : EntityNotFoundException(message) {
}