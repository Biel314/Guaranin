package br.edu.fatec.tcc.guaranin.controller.mascote

import br.edu.fatec.tcc.guaranin.controller.mascote.MascoteAPIController
import br.edu.fatec.tcc.guaranin.dto.ApiErrorDTO
import br.edu.fatec.tcc.guaranin.exception.mascote.MascoteNotFoundException
import br.edu.fatec.tcc.guaranin.exception.mascote.MascoteVivoException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Tratamento das exceções de domínio do [MascoteAPIController].
 *
 * Restrito ao controller via `assignableTypes` e com precedência sobre o
 * [br.edu.fatec.tcc.guaranin.controller.GlobalAPIExceptionHandler], que trata
 * as exceções padrão do sistema.
 *
 * @see MascoteAPIController
 * @see br.edu.fatec.tcc.guaranin.controller.GlobalAPIExceptionHandler
 */
@RestControllerAdvice(assignableTypes = [MascoteAPIController::class])
@Order(Ordered.HIGHEST_PRECEDENCE)
class MascoteAPIExceptionHandler {
    /**
     * Converte [MascoteNotFoundException] em `404 Not Found`.
     *
     * @return [ApiErrorDTO] com a mensagem "Mascote nao encontrado!".
     */
    @ExceptionHandler(MascoteNotFoundException::class)
    fun mascoteNotFoundException(ex: MascoteNotFoundException): ResponseEntity<ApiErrorDTO> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiErrorDTO(ex.localizedMessage))
    }

    /**
     * Converte [MascoteVivoException] em `409 Conflict`.
     *
     * @return [ApiErrorDTO] com a mensagem "Ja existe um mascote vivo...".
     */
    @ExceptionHandler(MascoteVivoException::class)
    fun mascoteVivoException(
        ex: MascoteVivoException
    ): ResponseEntity<ApiErrorDTO> {

        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ApiErrorDTO(error = ex.localizedMessage))
    }
}
