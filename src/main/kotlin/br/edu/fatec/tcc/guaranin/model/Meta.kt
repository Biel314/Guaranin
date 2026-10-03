package br.edu.fatec.tcc.guaranin.model

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import java.util.UUID

@Entity
@Table(name = "tb_meta")
class Meta(
    @Id
    @Column(name = "id_meta", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @Column(nullable = false)
    @Size(min = 3, max = 50, message = "O nome da meta deve ter entre 3 e 50 caracteres")
    val titulo: String,

    @Column(nullable = false)
    @Size(min = 3, max = 200, message = "A descricao da meta deve ter entre 3 e 200 caracteres")
    var descricao: String,

    @Column(name = "vencimento_dias", nullable = false)
    @field:Min(value = 1, message = "O vencimento deve ser de no mínimo 1 dia")
    @field:Max(value = 30, message = "O vencimento deve ser de no máximo 30 dias")
    var vencimentoDias: Int,

    @Column(name = "limite_acesso", nullable = true)
    @field:Min(value = 0, message = "O limite de acessos deve ser de no mínimo 0")
    @field:Max(value = 15, message = "O limite de acesso deve ser de no máximo 15 acessos diários")
    var limiteAcesso: Short,

    @Column(name = "tempo_uso", nullable = true)
    @field:Min(value = 0, message = "O tempo de uso deve ser de no mínimo 0 minutos")
    @field:Max(value = 120, message = "O tempo de uso deve ser de no máximo 120 minutos (2 horas)")
    var tempoUso: Int
) {
    constructor() : this(null, "", "", 0, 0, 0)
}