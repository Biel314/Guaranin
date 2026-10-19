package br.edu.fatec.tcc.guaranin.model

import br.edu.fatec.tcc.guaranin.validation.RegexPatterns
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
 * O e-mail é único e usado como credencial de login. A senha é write-only
 * e não é serializada nas respostas JSON. As validações de apelido, e-mail
 * e senha reutilizam os padrões definidos em [RegexPatterns].
 *
 * @property id identificador único do usuário, gerado automaticamente.
 * @property apelido apelido do usuário, com 3 a 26 caracteres.
 * @property email e-mail único do usuário, usado como login.
 * @property password senha de acesso, write-only, com 8 a 72 caracteres.
 * @property usuarioURLs URLs monitoradas associadas ao usuário.
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
    @field:Size(min = 3, max = 26, message = "O apelido deve ter entre 3 e 26 caracteres")
    @field:Pattern(
        regexp = RegexPatterns.USUARIO,
        message = "O apelido deve conter apenas letras, números, ponto, sublinhado e hífen, com 3 a 26 caracteres"
    )
    var apelido: String = "",

    @Column(nullable = false, unique = true, length = 254)
    @field:NotBlank(message = "O e-mail é obrigatório")
    @field:Email(regexp = RegexPatterns.EMAIL, message = "Formato de e-mail inválido")
    @field:Size(max = 254, message = "O e-mail deve ter no máximo 254 caracteres")
    var email: String = "",

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 255)
    @field:NotBlank(message = "A senha é obrigatória")
    @field:Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres")
    @field:Pattern(
        regexp = RegexPatterns.SENHA,
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

    /**
     * Alias de conveniência para [email], usado como login.
     * Ignorado na serialização JSON.
     */
    @get:JsonIgnore
    var login: String
        get() = this.email
        set(value) {
            this.email = value
        }
}