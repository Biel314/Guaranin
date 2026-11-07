package br.edu.fatec.tcc.guaranin.exception

class APIKeyInvalidException(
    message: String = "Invalid API Key."
) : APIException(message) {
}