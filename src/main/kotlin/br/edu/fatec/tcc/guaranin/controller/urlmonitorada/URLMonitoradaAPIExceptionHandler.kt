package br.edu.fatec.tcc.guaranin.controller.urlmonitorada

import br.edu.fatec.tcc.guaranin.dto.ApiErrorDTO
import br.edu.fatec.tcc.guaranin.exception.urlmonitorada.URLMonitoradaNotFoundException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Tratamento das exceções de domínio do [URLMonitoradaAPIController].
 *
 * Restrito ao controller via `assignableTypes` e com precedência sobre o
 * [br.edu.fatec.tcc.guaranin.controller.GlobalAPIExceptionHandler], que trata
 * as exceções padrão do sistema.
 *
 * @see URLMonitoradaAPIController
 * @see br.edu.fatec.tcc.guaranin.controller.GlobalAPIExceptionHandler
 */
@RestControllerAdvice(assignableTypes = [URLMonitoradaAPIController::class])
@Order(Ordered.HIGHEST_PRECEDENCE)
class URLMonitoradaAPIExceptionHandler {

    /**
     * Converte [URLMonitoradaNotFoundException] em `404 Not Found`.
     *
     * @return [ApiErrorDTO] com a mensagem "URL monitorada não encontrada".
     */
    @ExceptionHandler(URLMonitoradaNotFoundException::class)
    fun handleURLMonitoradaNotFound(ex: URLMonitoradaNotFoundException): ResponseEntity<ApiErrorDTO> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiErrorDTO(ex.localizedMessage))
    }
}
