package br.edu.fatec.tcc.guaranin.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.util.*

@Entity
class Usuario(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usuario", nullable = false)
    var id: UUID,

    @Column(length = 26)
    @NotBlank(message = "O Nome de Usuário é obrigatório")
    @Size(max = 26, message = "O login deve ter no máximo 26 caracteres")
    var apelido: String,

    @Column(nullable = false, unique = true, length = 254)
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(regexp = "\\w+@\\w+\\.\\w+", message = "Formato de e-mail inválido")
    @Size(max = 254, message = "O login deve ter no máximo 254 caracteres")
    var email: String,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 255)
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,72}$",
        message = "A senha deve ter entre 8 e 72 caracteres e conter ao menos uma letra maiúscula, uma minúscula, um número e um caractere especial"
    )
    var password: String,

    // TODO External Connections
    @OneToOne(cascade = [(CascadeType.ALL)])
    var mascote: Mascote?,

    @OneToMany(mappedBy = "usuario", cascade = [(CascadeType.ALL)])
    val metas: MutableList<Metas> = mutableListOf(),

    @OneToMany(mappedBy = "usuario", cascade = [(CascadeType.ALL)])
    val historicoSesses: MutableList<HistoricoSessoesUso> = mutableListOf()
) {
    @JsonIgnore
    public fun getLogin(): String {
        return this.email
    }
}