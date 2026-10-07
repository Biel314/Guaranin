package br.edu.fatec.tcc.guaranin.model

import jakarta.persistence.*
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.util.UUID

@Entity
class Mascote(
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @Column(name = "nome", nullable = false)
    @Size(
        min = 3, max = 255,
        message = "O nome do mascote deve ter entre 3 e 255 caracteres"
    )
    var nome: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    var estado: Estado,

    @Enumerated(EnumType.STRING)
    @Column(name = "humor", nullable = false)
    var humor: Humor,

    @Column(name = "progresso", nullable = false)
    var nivel: Int,

    @Column(name = "experiencia", nullable = false)
    var experiencia: Int,

    @Column(nullable = false)
    @Min (
        value = 0,
        message = "pontos de vida não podem ser menor que 0"
    )
    @Max(
        value = 7,
        message = "pontos de vida não podem passar de 7"
    )
    var pontosVida: Int,

    @Column(name = "data_morte", nullable = true)
    var dataMorte: LocalDate? = null
) {

}