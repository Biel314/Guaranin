package br.edu.fatec.tcc.guaranin.model

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID

class AplicativoMonitorado (
    @Id
    @Column(name="id_aplicativo_monitorado", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
   var id: UUID? = null,

    )
{

}