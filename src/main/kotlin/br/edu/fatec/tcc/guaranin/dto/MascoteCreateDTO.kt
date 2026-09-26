package br.edu.fatec.tcc.guaranin.dto

import br.edu.fatec.tcc.guaranin.model.Estado
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class MascoteCreateDTO (
    @NotBlank(message = "O nome é obrigatorio")
    @Size(max = 255, message = "O nome deve possuir no máximo 255 caracteres")
    val nome: String,

    val estado: Estado,

    val pontosVida: Int
) {}