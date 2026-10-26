package br.edu.fatec.tcc.guaranin.controller.urlmonitorada

import br.edu.fatec.tcc.guaranin.dto.ApiErrorDTO
import br.edu.fatec.tcc.guaranin.exception.urlmonitorada.URLHistoricoNotFoundException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Tratamento das exceções de domínio do [URLHistoricoAPIController].
 *
 * Restrito ao controller via `assignableTypes` e com precedência sobre o
 * [br.edu.fatec.tcc.guaranin.controller.GlobalAPIExceptionHandler], que trata
 * as exceções padrão do sistema.
 *
 * @see URLHistoricoAPIController
 * @see br.edu.fatec.tcc.guaranin.controller.GlobalAPIExceptionHandler
 */
@RestControllerAdvice(assignableTypes = [URLHistoricoAPIController::class])
@Order(Ordered.HIGHEST_PRECEDENCE)
class URLHistoricoAPIExceptionHandler {

    /**
     * Converte [URLHistoricoNotFoundException] em `404 Not Found`.
     *
     * @param ex exceção lançada quando o histórico não existe.
     * @return [ApiErrorDTO] com a mensagem "URL histórico não encontrado".
     */
    @ExceptionHandler(URLHistoricoNotFoundException::class)
    fun handleURLHistoricoNotFoundException(ex: URLHistoricoNotFoundException): ResponseEntity<ApiErrorDTO> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiErrorDTO(ex.localizedMessage))
    }
}