package br.edu.fatec.tcc.guaranin.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tb_aplicativo_historico")
class AplicativoHistorico (
    @Id
    @Column(name="id_aplicativo_historico", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_aplicativo", nullable = false)
    @Column(name="id_usuario_aplicativo", nullable = false)
    var usuarioAplicativo: UsuarioAplicativo,

    @Column(name="data_hora_acesso", nullabe = true)
    var dataHoraAcesso: LocalDateTime

) {
}