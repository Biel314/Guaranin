package br.edu.fatec.tcc.guaranin.exception

import jakarta.persistence.EntityNotFoundException

class URLMonitoradaNotFoundException(
    message: String = "URL not found",
) : EntityNotFoundException(message) {
}