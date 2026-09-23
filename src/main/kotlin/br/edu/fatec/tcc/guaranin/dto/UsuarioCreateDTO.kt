package br.edu.fatec.tcc.guaranin.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UsuarioCreateDTO(
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(regexp = "\\w+@\\w+\\.\\w+", message = "Formato de e-mail inválido")
    @Size(max = 255, message = "O login deve ter no máximo 255 caracteres")
    val login: String,

    @Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,72}$",
        message = "A senha deve ter entre 8 e 72 caracteres e conter ao menos uma letra maiúscula, uma minúscula, um número e um caractere especial"
    )
    val password: String,
) {}