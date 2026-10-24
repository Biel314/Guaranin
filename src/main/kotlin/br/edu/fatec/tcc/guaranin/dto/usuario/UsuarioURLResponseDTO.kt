package br.edu.fatec.tcc.guaranin.dto.usuario

import java.util.*

/**
 * DTO de resposta da API para [br.edu.fatec.tcc.guaranin.model.UsuarioURL].
 *
 * Expõe os identificadores das associações em vez das entidades JPA completas.
 *
 * @property id identificador único da associação.
 * @property usuarioId identificador do usuário associado.
 * @property urlMonitoradaId identificador da URL monitorada associada.
 * @property qtdAcesso quantidade de acessos registrada.
 * @property tempoAtivo tempo ativo em segundos.
 */
data class UsuarioURLResponseDTO(
    var id: UUID,
    var usuarioId: UUID,
    var urlMonitoradaId: UUID,
    var qtdAcesso: Short,
    var tempoAtivo: Int
) {}
