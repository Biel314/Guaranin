package br.edu.fatec.tcc.guaranin.model

import jakarta.persistence.*

@Entity
data class HistoricoSessoesUso(
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
) {
    constructor() : this(null)
}