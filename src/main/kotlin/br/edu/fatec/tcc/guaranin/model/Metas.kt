package br.edu.fatec.tcc.guaranin.model

import jakarta.persistence.*
import jakarta.validation.constraints.Size

@Entity
data class Metas(
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @Column(nullable = false)
    @Size(min = 3, max = 255, message = "O nome da meta deve ter entre 3 e 255 caracteres")
    val nome: String,

    @Column(nullable = false)
    @Size(min = 3, max = 255, message = "A descricao da meta deve ter entre 3 e 255 caracteres")
    var descricao: String
) {
    constructor() : this(null, "", "")
}