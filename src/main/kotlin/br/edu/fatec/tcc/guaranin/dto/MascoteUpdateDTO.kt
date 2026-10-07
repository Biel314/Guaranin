package br.edu.fatec.tcc.guaranin.dto

import br.edu.fatec.tcc.guaranin.model.Estado
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.util.UUID

data class MascoteUpdateDTO (
    @NotNull
    var id: UUID?,

    @NotBlank(message = "O nome é obrigatório")
    @Size(min= 3, max = 255, message = "O nome deve possuir entre 3 e 255 caracteres")
    var nome: String?,

) {}
