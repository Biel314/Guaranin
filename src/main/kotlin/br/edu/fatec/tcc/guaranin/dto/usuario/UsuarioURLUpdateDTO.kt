package br.edu.fatec.tcc.guaranin.dto.usuario

import jakarta.validation.constraints.Min

/**
 * DTO de entrada para atualização parcial de
 * [br.edu.fatec.tcc.guaranin.model.UsuarioURL].
 *
 * O identificador do registro é informado no caminho da requisição. Campos
 * nulos preservam os valores atuais; as associações são resolvidas pelo
 * service quando seus identificadores são informados.
 *
 * @property qtdAcesso nova quantidade de acessos; opcional e não negativa.
 * @property tempoAtivo novo tempo ativo em segundos; opcional e não negativo.
 */
data class UsuarioURLUpdateDTO(

    @field:Min(value = 0, message = "A quantidade de acesso deve ser maior ou igual a zero")
    val qtdAcesso: Short?,

    @field:Min(value = 0, message = "O tempo ativo deve ser maior ou igual a zero")
    val tempoAtivo: Int?
)
