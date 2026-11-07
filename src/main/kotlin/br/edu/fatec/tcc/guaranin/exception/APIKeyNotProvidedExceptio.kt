package br.edu.fatec.tcc.guaranin.exception

class APIKeyNotProvidedException(
    message: String = "API Key not provided"
) : APIException(message) {
}