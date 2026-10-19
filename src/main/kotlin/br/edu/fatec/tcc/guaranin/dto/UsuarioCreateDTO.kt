package br.edu.fatec.tcc.guaranin.dto

import br.edu.fatec.tcc.guaranin.validation.RegexPatterns
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

/**
 * DTO de entrada para criação de um usuário
 * ([br.edu.fatec.tcc.guaranin.model.Usuario]).
 *
 * Os campos são validados com Jakarta Bean Validation antes da conversão
 * para entidade. Os padrões de apelido, e-mail e senha são centralizados
 * em [RegexPatterns].
 *
 * @property apelido apelido do usuário, com 3 a 26 caracteres.
 * @property email e-mail único do usuário, com até 254 caracteres.
 * @property password senha de acesso, com 8 a 72 caracteres e ao menos
 *   uma maiúscula, uma minúscula, um número e um caractere especial.
 */
data class UsuarioCreateDTO(
    @field:Schema(
        description = "Apelido do usuário",
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
    var apelido: String,

    @field:Schema(
        description = "E-mail único do usuário",
        example = "usuario@fatec.br",
        maxLength = 254
    )
    @field:NotBlank(message = "O e-mail é obrigatório")
    @field:Email(regexp = RegexPatterns.EMAIL, message = "Formato de e-mail inválido")
    @field:Size(max = 254, message = "O e-mail deve ter no máximo 254 caracteres")
    val email: String,

    @field:Schema(
        description = "Senha de acesso",
        example = "usuario@123",
        minLength = 8,
        maxLength = 72,
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    @field:Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres")
    @field:Pattern(
        regexp = RegexPatterns.SENHA,
        message = "A senha deve ter entre 8 e 72 caracteres e conter ao menos uma letra maiúscula, uma minúscula, um número e um caractere especial"
    )
    val password: String,
) {}