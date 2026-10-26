package br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado

import java.time.LocalDateTime
import java.util.UUID

/**
 * DTO de entrada utilizado para criação de registros de
 * [AplicativoHistorico].
 *
 * Os identificadores recebidos são resolvidos para entidades pelo
 * service antes da persistência.
 *
 * @property usuarioAplicativoId identificador da associação
 * usuário-aplicativo responsável pelo acesso.
 * @property dataHoraAcesso data e hora em que o acesso ocorreu.
 */
class AplicativoHistoricoCreateDTO(

    val usuarioAplicativoId: UUID,

    val dataHoraAcesso: LocalDateTime
)