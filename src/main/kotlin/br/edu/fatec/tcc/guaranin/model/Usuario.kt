package br.edu.fatec.tcc.guaranin.model

import jakarta.persistence.Entity
import jakarta.persistence.OneToOne

@Entity
class Usuario {
    var id: Long? = null
    var login: String = ""
    var password: String = ""

    @OneToOne
    var mascote: Mascote? = null
}