package br.edu.fatec.tcc.guaranin.exception.security

class PasswordEncodingException(message:String = "Falha ao codificar a senha do usuário."): RuntimeException(message)