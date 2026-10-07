package br.edu.fatec.tcc.guaranin.exception

import jakarta.persistence.EntityNotFoundException

/**
 * Exceção lançada quando uma entidade [br.edu.fatec.tcc.guaranin.model.URLMonitorada]
 * não é encontrada no repositório.
 *
 * Herda de [EntityNotFoundException] para mapeamento automático em respostas HTTP apropriadas.
 *
 * @property message mensagem descritiva do erro (padrão "URL not found").
 */
class URLMonitoradaNotFoundException(
    message: String = "URL not found",
) : EntityNotFoundException(message) {
}