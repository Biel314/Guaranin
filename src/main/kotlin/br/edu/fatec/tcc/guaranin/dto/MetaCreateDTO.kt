package br.edu.fatec.tcc.guaranin.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class MetaCreateDTO (
    @NotBlank(message = "O Titulo é obrigatório")
    @Size(min = 3, max = 50, message = "O nome da meta deve ter entre 3 e 50 caracteres")
    val titulo: String,

    @Size(min = 0, max = 200, message = "A descricao da meta deve ter entre 3 e 200 caracteres")
    var descricao: String,

    @field:Min(value = 1, message = "O vencimento deve ser de no mínimo 1 dia")
    @field:Max(value = 30, message = "O vencimento deve ser de no máximo 30 dias")
    var vencimento_dias: Int,

    @field:Min(value = 0, message = "O limite de acessos deve ser de no mínimo 0")
    @field:Max(value = 15, message = "O limite de acesso deve ser de no máximo 15 acessos diários")
    var limite_acesso: Short,

    @field:Min(value = 0, message = "O tempo de uso deve ser de no mínimo 0 minutos")
    @field:Max(value = 120, message = "O tempo de uso deve ser de no máximo 120 minutos (2 horas)")
    var tempo_uso: Int
) {}