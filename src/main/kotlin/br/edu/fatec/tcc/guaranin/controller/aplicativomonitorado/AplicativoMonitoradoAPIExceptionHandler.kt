package br.edu.fatec.tcc.guaranin.controller.aplicativomonitorado

import br.edu.fatec.tcc.guaranin.dto.ApiErrorDTO
import br.edu.fatec.tcc.guaranin.exception.aplicativomonitorado.AplicativoMonitoradoNotFoundException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Tratamento das exceções de domínio do [AplicativoMonitoradoAPIController].
 *
 * Restrito ao controller via `assignableTypes` e com precedência sobre o
 * [br.edu.fatec.tcc.guaranin.controller.GlobalAPIExceptionHandler], que trata
 * as exceções padrão do sistema.
 *
 * @see AplicativoMonitoradoAPIController
 * @see br.edu.fatec.tcc.guaranin.controller.GlobalAPIExceptionHandler
 */
@RestControllerAdvice(assignableTypes = [AplicativoMonitoradoAPIController::class])
@Order(Ordered.HIGHEST_PRECEDENCE)
class AplicativoMonitoradoAPIExceptionHandler {
    /**
     * Converte [AplicativoMonitoradoNotFoundException] em `404 Not Found`.
     *
     * @return [ApiErrorDTO] com a mensagem "Aplicativo monitorado não encontrado".
     */
    @ExceptionHandler(AplicativoMonitoradoNotFoundException::class)
    fun aplicativoMonitoradoNotFound(ex: AplicativoMonitoradoNotFoundException): ResponseEntity<ApiErrorDTO> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiErrorDTO(ex.localizedMessage))
    }
}
