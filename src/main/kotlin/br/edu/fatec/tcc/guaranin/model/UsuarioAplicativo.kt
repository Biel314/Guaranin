package br.edu.fatec.tcc.guaranin.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "tb_usuario_aplicativo")
class UsuarioAplicativo (
    @Id
    @Column(name="id_usuario_aplicativo", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    var usuario: Usuario,

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aplicativo_monitorado", nullable = false)
    var aplicativoMonitorado: AplicativoMonitorado,

    @Column(name = "qtd_acesso", nullable = false)
    var qtdAcesso: Int = 0,

    @Column(name = "tempo_ativo", nullable = false)
    var tempoAtivo: Int = 0

    )
{

}