package br.edu.fatec.tcc.guaranin.model

import jakarta.persistence.*
import jakarta.validation.constraints.FutureOrPresent
import java.time.LocalDateTime
import java.util.*

@Table(name = "tb_url_historico")
@Entity
class UrlHistorico(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @Column(nullable = false)
    var id_usuario_url: UUID? = null,

    @Column(nullable = false)
    @FutureOrPresent
    var dataHoraAcesso: LocalDateTime = LocalDateTime.now(),
) {
}