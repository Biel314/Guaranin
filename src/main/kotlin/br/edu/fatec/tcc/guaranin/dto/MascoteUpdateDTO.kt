package br.edu.fatec.tcc.guaranin.dto

import br.edu.fatec.tcc.guaranin.model.Estado
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.util.UUID

data class MascoteUpdateDTO (

    var id: UUID?,

    @NotBlank(message = "O e-nome é obrigatório")
    @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
    var nome: String?,

    var estado: Estado,

    var pontosVida: UByte
) {}
