package br.edu.fatec.tcc.guaranin.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.util.Optional
import java.util.UUID

@Entity
@Table(
    name = "tb_usuario_aplicativo",
    uniqueConstraints = [
        UniqueConstraint(
            columnNames = [
                "id_usuario",
                "id_aplicativo_monitorado"
            ]
        )
    ]
)
class UsuarioAplicativo (
    @Id
    @Column(name="id_usuario_aplicativo", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    var usuario: Usuario,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aplicativo_monitorado", nullable = false)
    var aplicativoMonitorado: AplicativoMonitorado,

    @Column(name = "qtd_acesso", nullable = false)
    var qtdAcesso: Int = 0,

    @Column(name = "tempo_ativo", nullable = false)
    var tempoAtivo: Int = 0

    )
{

}