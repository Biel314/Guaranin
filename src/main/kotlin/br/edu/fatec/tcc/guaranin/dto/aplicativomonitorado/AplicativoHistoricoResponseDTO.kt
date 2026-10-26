package br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado

import java.time.LocalDateTime
import java.util.UUID

/**
 * DTO de saída utilizado para representar registros de
 * [AplicativoHistorico].
 *
 * Contém os dados necessários para consulta dos acessos realizados
 * por um usuário a um aplicativo monitorado.
 *
 * @property id identificador único do registro histórico.
 * @property usuarioAplicativoId identificador da associação
 * usuário-aplicativo relacionada ao acesso.
 * @property dataHoraAcesso data e hora em que o acesso foi registrado.
 */
class AplicativoHistoricoResponseDTO(

    val id: UUID,

    val usuarioAplicativoId: UUID,

    val dataHoraAcesso: LocalDateTime
)
{

}