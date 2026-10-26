package br.edu.fatec.tcc.guaranin.exception.aplicativomonitorado

import jakarta.persistence.EntityNotFoundException

class AplicativoHistorico (
    message: String = "Historico de Aplicativo not found!"
    ) : EntityNotFoundException(message)
{
}