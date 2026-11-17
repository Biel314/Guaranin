package br.edu.fatec.tcc.guaranin.exception.aplicativomonitorado

import jakarta.persistence.EntityNotFoundException

class AplicativoMonitoradoNotFoundException (
        message: String = "Aplicativo Monitorado not found!"
    ) : EntityNotFoundException(message)

{
}