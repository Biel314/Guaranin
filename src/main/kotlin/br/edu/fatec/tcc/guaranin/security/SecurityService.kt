package br.edu.fatec.tcc.guaranin.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
interface SecurityService {
    fun getUsuarioLogadoId(): UUID
}
