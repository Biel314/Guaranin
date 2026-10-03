package br.edu.fatec.tcc.guaranin.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.util.*

/**
 * DTO de entrada para atualização de [Usuario].
 */
data class UsuarioUpdateDTO(

    var id: UUID,

    @field:NotBlank(message = "O Nome de Usuário é obrigatório")
    @field:Size(max = 26, message = "O apelido deve ter no máximo 26 caracteres")
    var apelido: String?,

    @field:NotBlank(message = "O e-mail é obrigatório")
    @field:Email(regexp = "\\w+@\\w+\\.\\w+", message = "Formato de e-mail inválido")
    @field:Size(max = 254, message = "O e-mail deve ter no máximo 254 caracteres")
    var email: String?,

    @field:Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,72}$",
        message = "A senha deve ter entre 8 e 72 caracteres e conter ao menos uma letra maiúscula, uma minúscula, um número e um caractere especial"
    )
    var password: String?,
) {}
