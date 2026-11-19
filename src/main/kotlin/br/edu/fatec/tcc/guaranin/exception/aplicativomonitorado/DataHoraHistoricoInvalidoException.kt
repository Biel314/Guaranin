package br.edu.fatec.tcc.guaranin.exception.aplicativomonitorado

import jakarta.persistence.EntityNotFoundException

class DataHoraHistoricoInvalidoException(
    message: String = "Data e hora invalidas. Nao foi possivel registrar no historico"
) : EntityNotFoundException(message) {
}