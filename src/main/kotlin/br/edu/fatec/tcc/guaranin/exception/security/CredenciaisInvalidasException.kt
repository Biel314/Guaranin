package br.edu.fatec.tcc.guaranin.exception.security

import javax.security.auth.login.CredentialException

class CredenciaisInvalidasException (
    message: String = "Credenciais invalidas"
): CredentialException(message) {

}