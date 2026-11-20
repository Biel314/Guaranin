package br.edu.fatec.tcc.guaranin.security

import java.util.*

interface SecurityService {
    fun getUsuarioLogadoId(): UUID
    fun validarUsuario(usuarioId: UUID?): Boolean
}
