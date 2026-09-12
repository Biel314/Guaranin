package br.edu.fatec.tcc.guaranin.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Entity
class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(nullable = false, unique = true, length = 255)
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(regexp = "\\w+@\\w+\\.\\w+",message = "Formato de e-mail inválido")
    @Size(max = 255, message = "O login deve ter no máximo 255 caracteres")
    var login: String? = null

    @Column(nullable = false, length = 255)
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 8, max = 72, message = "A senha deve ter no máximo 255 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,72}$",
        message = "A senha deve ter entre 8 e 72 caracteres e conter ao menos uma letra maiúscula, uma minúscula, um número e um caractere especial"
    )
    var password: String? = null

    @OneToOne(cascade = [(CascadeType.ALL)])
    var mascote: Mascote? = null

    @OneToMany(cascade = [(CascadeType.ALL)])
    val metas: MutableList<MetasEDesafios> = mutableListOf()

    @OneToMany(cascade = [(CascadeType.ALL)])
    val historicoSesses: MutableList<HistoricoSessoesUso> = mutableListOf()
}