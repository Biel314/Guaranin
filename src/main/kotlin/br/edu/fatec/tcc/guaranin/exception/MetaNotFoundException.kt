package br.edu.fatec.tcc.guaranin.exception

import jakarta.persistence.EntityNotFoundException

class MetaNotFoundException (
    message: String = "Meta not found!"
) : EntityNotFoundException(message) {
}