package br.edu.fatec.tcc.guaranin.model

import jakarta.persistence.*

@Entity
class HistoricoSessoesUso(
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    var usuario: Usuario? = null
) {
}