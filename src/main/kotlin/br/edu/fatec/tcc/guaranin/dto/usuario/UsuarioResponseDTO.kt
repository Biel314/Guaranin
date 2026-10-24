package br.edu.fatec.tcc.guaranin.dto.usuario

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

/**
 * DTO de resposta da API para [br.edu.fatec.tcc.guaranin.model.Usuario].
 *
 * Expõe apenas os dados públicos do usuário, sem a senha.
 *
 * @property id identificador único do usuário.
 * @property apelido apelido do usuário.
 * @property email e-mail do usuário.
 */
data class UsuarioResponseDTO(
    @field:Schema(
        description = "Identificador único do usuário",
        example = "550e8400-e29b-41d4-a716-446655440000"
    )
    var id: UUID,

    @field:Schema(
        description = "Apelido do usuário",
        example = "usuario",
        minLength = 3,
        maxLength = 26
    )
    var apelido: String,

    @field:Schema(
        description = "E-mail do usuário",
        example = "usuario@fatec.br",
        maxLength = 254
    )
    var email: String,
) {}