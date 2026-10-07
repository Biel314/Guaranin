package br.edu.fatec.tcc.guaranin.exception

/**
 * Exceção disparada quando um DTO é inválido ou possui dados incorretos.
 */
class InvalidDTOException(
    message: String = "DTO inválido!"
) : IllegalArgumentException(message) {
}
