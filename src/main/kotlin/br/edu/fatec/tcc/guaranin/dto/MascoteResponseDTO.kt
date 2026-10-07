package br.edu.fatec.tcc.guaranin.dto

import br.edu.fatec.tcc.guaranin.model.Estado
import br.edu.fatec.tcc.guaranin.model.Humor
import java.time.LocalDate
import java.util.UUID

data class MascoteResponseDTO(
    val id: UUID,
    val usuarioId: UUID,
    val nome: String,
    val estado: Estado,
    val humor: Humor,
    val nivel: Int,
    val experiencia: Int,
    val pontosVida: Int,
    val dataMorte: LocalDate?
) {}