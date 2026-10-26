package br.edu.fatec.tcc.guaranin.model

import jakarta.persistence.*
import jakarta.validation.constraints.PastOrPresent
import java.time.LocalDateTime
import java.util.*

/**
 * Entidade JPA que representa um registro do histórico de acesso a URLs.
 *
 * Cada instância corresponde a um acesso registrado para uma associação
 * [UsuarioURL] e é persistida na tabela `tb_url_historico`. A data/hora do
 * acesso deve ser presente ou passada e, por padrão, é inicializada no
 * instante da criação do registro.
 *
 * @property id identificador único do histórico, gerado automaticamente.
 * @property usuarioUrl associação [UsuarioURL] que originou o acesso.
 * @property dataHoraAcesso data e hora em que o acesso ocorreu; deve ser
 * presente ou passada.
 * @see UsuarioURL
 */
@Entity
@Table(name = "tb_url_historico")
class URLHistorico(
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
