package br.edu.fatec.tcc.guaranin.model

import jakarta.persistence.*
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import java.util.UUID

@Entity
class Mascote(
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @Column(nullable = false)
    @Size(
        min = 3, max = 255,
        message = "O nome do mascote deve ter entre 3 e 255 caracteres"
    )
    var nome: String? = null,

    @Column(nullable = false)
    var estado: Estado,

    @Column(nullable = false)
    @Min (
        value = 0,
        message = "pontos de vida não podem ser menor que 0"
    )
    @Max(
        value = 7,
        message = "pontos de vida não podem passar de 7"
    )
    var pontosVida: Int
) {
    constructor() : this(null, "",Estado.VIVO, 7)
}