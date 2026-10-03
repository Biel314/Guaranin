package br.edu.fatec.tcc.guaranin.dto

import java.util.*

/**
 * DTO de resposta da API para [Usuario].
 */
data class UsuarioResponseDTO(
    var id: UUID,
    var apelido: String,
    var email: String,
) {}