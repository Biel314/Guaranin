package br.edu.fatec.tcc.guaranin.dto.urlmonitorada

import java.time.LocalDateTime
import java.util.*

/**
 * DTO de resposta da API para [br.edu.fatec.tcc.guaranin.model.URLHistorico].
 *
 * Expõe apenas os dados necessários ao cliente, sem carregar a entidade JPA
 * ou suas associações.
 *
 * @property id identificador único do histórico.
 * @property usuarioUrlId identificador da associação
 * [br.edu.fatec.tcc.guaranin.model.UsuarioURL].
 * @property dataHoraAcesso data e hora em que o acesso ocorreu.
 */
data class URLHistoricoResponseDTO(
    var id: UUID,
    var usuarioUrlId: UUID,
    var dataHoraAcesso: LocalDateTime
) {}
