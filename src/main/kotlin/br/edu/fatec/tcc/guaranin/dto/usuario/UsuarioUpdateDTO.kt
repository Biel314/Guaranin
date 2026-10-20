package br.edu.fatec.tcc.guaranin.dto.usuario

import br.edu.fatec.tcc.guaranin.validation.RegexPatterns
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*
import java.util.*

/**
 * DTO de entrada para atualização de um usuário
 * ([br.edu.fatec.tcc.guaranin.model.Usuario]).
 *
 * O [id] identifica o registro a ser atualizado. Apelido, e-mail e senha
 * reutilizam os padrões de [RegexPatterns]. A senha é opcional e deve ser
 * informada apenas quando houver troca.
 *
 * @property id identificador único obrigatório do usuário a ser atualizado.
 * @property apelido novo apelido, com 3 a 26 caracteres.
 * @property email novo e-mail único, com até 254 caracteres.
 * @property password nova senha opcional, com 8 a 72 caracteres e ao menos
 *   uma maiúscula, uma minúscula, um número e um caractere especial.
 */
data class UsuarioUpdateDTO(

    @field:Schema(
        description = "Identificador único do usuário a ser atualizado",
        example = "550e8400-e29b-41d4-a716-446655440000"
    )
    @field:NotNull
    var id: UUID? = null,

    @field:Schema(
        description = "Novo apelido do usuário",
        example = "usuario",
        minLength = 3,
        maxLength = 26
    )
    @field:NotBlank(message = "O Nome de Usuário é obrigatório")
    @field:Size(min = 3, max = 26, message = "O apelido deve ter entre 3 e 26 caracteres")
    @field:Pattern(
        regexp = RegexPatterns.USUARIO,
        message = "O apelido deve conter apenas letras, números, ponto, sublinhado e hífen, com 3 a 26 caracteres"
    )
    var apelido: String?,

    @field:Schema(
        description = "Novo e-mail do usuário",
        example = "usuario@fatec.br",
        maxLength = 254
    )
    @field:NotBlank(message = "O e-mail é obrigatório")
    @field:Email(regexp = RegexPatterns.EMAIL, message = "Formato de e-mail inválido")
    @field:Size(max = 254, message = "O e-mail deve ter no máximo 254 caracteres")
    var email: String?,

    @field:Schema(
        description = "Nova senha opcional do usuário",
        example = "usuario@123",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    @field:Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres")
    @field:Pattern(
        regexp = RegexPatterns.SENHA,
        message = "A senha deve ter entre 8 e 72 caracteres e conter ao menos uma letra maiúscula, uma minúscula, um número e um caractere especial"
    )
    var password: String?,
) {}
