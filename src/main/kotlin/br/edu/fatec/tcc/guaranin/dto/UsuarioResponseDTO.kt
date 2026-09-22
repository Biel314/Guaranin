package br.edu.fatec.tcc.guaranin.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UsuarioResponseDTO(
    var id: Long?,

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(regexp = "\\w+@\\w+\\.\\w+", message = "Formato de e-mail inválido")
    @Size(max = 255, message = "O login deve ter no máximo 255 caracteres")
    var login: String?
) {}