package br.edu.fatec.tcc.guaranin.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.Size
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "tb_aplicativo_monitorado")
class AplicativoMonitorado (
    @Id
    @Column(name="id_aplicativo_monitorado", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @Column(name="pacote", nullable = false, unique = true)
    @Size(min = 3, max = 255)
    var pacote: String = "",

    @Column(name="nome", nullable = false)
    @Size(min = 3, max = 50)
    var nome: String = "",

    @Column(name="padrao", nullable = false)
    var padrao: Boolean = false
    )
{

}