package br.edu.fatec.tcc.guaranin.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Entity
class Usuario(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(nullable = false, unique = true, length = 255)
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(regexp = "\\w+@\\w+\\.\\w+", message = "Formato de e-mail inválido")
    @Size(max = 255, message = "O login deve ter no máximo 255 caracteres")
    var login: String? = null,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 255)
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,72}$",
        message = "A senha deve ter entre 8 e 72 caracteres e conter ao menos uma letra maiúscula, uma minúscula, um número e um caractere especial"
    )
    var password: String? = null,

    @OneToOne(cascade = [(CascadeType.ALL)])
    var mascote: Mascote? = null,

    @OneToMany(mappedBy = "usuario", cascade = [(CascadeType.ALL)])
    val metas: MutableList<Metas> = mutableListOf(),

    @OneToMany(mappedBy = "usuario", cascade = [(CascadeType.ALL)])
    val historicoSesses: MutableList<HistoricoSessoesUso> = mutableListOf()
) {
    constructor() : this(null, "", "")

    @JsonIgnore
    public fun getEmail(): String? {
        return this.login
    }
}