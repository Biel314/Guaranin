package br.edu.fatec.tcc.guaranin.model

import jakarta.persistence.*
import jakarta.validation.constraints.PastOrPresent
import java.time.LocalDateTime
import java.util.*

/**
 * Entidade JPA que representa o histórico de acesso a URLs por usuários.
 *
 * @property id identificador único do histórico, gerado automaticamente.
 * @property usuarioUrl associação com a relação [UsuarioURL].
 * @property dataHoraAcesso data e hora em que o acesso ocorreu.
 */
@Entity
@Table(name = "tb_url_historico")
class UrlHistorico(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_url_historico", nullable = false)
    var id: UUID? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_url", nullable = false)
    var usuarioUrl: UsuarioURL? = null,

    @Column(name = "data_hora_acesso", nullable = false)
    @field:PastOrPresent(message = "A data e hora do acesso devem estar no passado ou presente")
    var dataHoraAcesso: LocalDateTime = LocalDateTime.now()
) {}