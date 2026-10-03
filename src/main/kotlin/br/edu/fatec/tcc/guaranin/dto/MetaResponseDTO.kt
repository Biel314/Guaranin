package br.edu.fatec.tcc.guaranin.dto

import java.util.*

data class MetaResponseDTO (
    var id: UUID,
    val titulo: String,
    var descricao: String,
    var vencimentoDias: Int,
    var limiteAcesso: Short,
    var tempoUso: Int
){}