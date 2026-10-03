package br.edu.fatec.tcc.guaranin.model

import jakarta.persistence.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.util.*

/**
 * Entidade JPA que representa a relação entre [Usuario] e [URLMonitorada].
 *
 * Armazena métricas de uso como a quantidade de acessos e o tempo ativo.
 *
 * @property id identificador único da associação, gerado automaticamente.
 * @property usuario usuário associado ao monitoramento.
 * @property urlMonitorada URL monitorada associada.
 * @property qtdAcesso quantidade de acessos realizados.
 * @property tempoAtivo tempo total ativo em segundos.
 */
@Entity
@Table(name = "tb_usuario_url")
class UsuarioURL(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usuario_url", nullable = false)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @field:NotNull(message = "O usuário é obrigatório")
    var usuario: Usuario? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_url_monitorado", nullable = false)
    @field:NotNull(message = "A URL monitorada é obrigatória")
    var urlMonitorada: URLMonitorada? = null,

    @Column(name = "qtd_acesso", nullable = false)
    @field:NotNull(message = "A quantidade de acesso é obrigatória")
    @field:Min(value = 0, message = "A quantidade de acesso deve ser maior ou igual a zero")
    var qtdAcesso: Short = 0,

    @Column(name = "tempo_ativo", nullable = false)
    @field:NotNull(message = "O tempo ativo é obrigatório")
    @field:Min(value = 0, message = "O tempo ativo deve ser maior ou igual a zero")
    var tempoAtivo: Int = 0
) {}
