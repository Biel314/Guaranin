package br.edu.fatec.tcc.guaranin.model

import jakarta.persistence.*
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

    var estado: Estado ?= null
) {
    constructor() : this(null, "")
}