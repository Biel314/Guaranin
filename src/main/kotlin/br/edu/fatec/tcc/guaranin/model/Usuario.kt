package br.edu.fatec.tcc.guaranin.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.util.*

/**
 * Entidade JPA que representa um usuário no sistema Guaranin.
 *
 * @property id UUID gerado automaticamente.
 * @property apelido Apelido do usuário (máx 26 chars).
 * @property email E-mail único (utilizado como login).
 * @property password Senha (write-only).
 * @property mascote Mascote vinculado.
 * @property metas Metas do usuário.
 * @property historicoSesses Histórico de sessões de uso.
 */
@Entity
@Table(name = "tb_usuario")
open class Usuario(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usuario", nullable = false)
    var id: UUID? = null,

    @Column(length = 26)
    @field:NotBlank(message = "O Nome de Usuário é obrigatório")
    @field:Size(max = 26, message = "O login deve ter no máximo 26 caracteres")
    var apelido: String = "",

    @Column(nullable = false, unique = true, length = 254)
    @field:NotBlank(message = "O e-mail é obrigatório")
    @field:Email(regexp = "\\w+@\\w+\\.\\w+", message = "Formato de e-mail inválido")
    @field:Size(max = 254, message = "O e-mail deve ter no máximo 254 caracteres")
    var email: String = "",

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 255)
    @field:NotBlank(message = "A senha é obrigatória")
    @field:Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,72}$",
        message = "A senha deve ter entre 8 e 72 caracteres e conter ao menos uma letra maiúscula, uma minúscula, um número e um caractere especial"
    )
    var password: String = "",

    // TODO External Connections
    //OneToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
    //var mascote: Mascote?,

    //@OneToMany(mappedBy = "usuario", cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)
    //val metas: MutableList<Metas> = mutableListOf(),

    //@OneToMany(mappedBy = "usuario", cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)
    //val historicoSessoes: MutableList<HistoricoSessoesUso> = mutableListOf(),

    @OneToMany(mappedBy = "usuario", cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)
    val usuarioURLs: MutableList<UsuarioURL> = mutableListOf()
) {
    constructor() : this(null, "", "", "", mutableListOf())

    @get:JsonIgnore
    var login: String
        get() = this.email
        set(value) {
            this.email = value
        }
}