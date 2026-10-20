package br.edu.fatec.tcc.guaranin.controller.usuario

import br.edu.fatec.tcc.guaranin.dto.ApiErrorDTO
import br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioNotFoundException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Tratamento das exceções de domínio do [UsuarioAPIController].
 *
 * Restrito ao controller via `assignableTypes` e com precedência sobre o
 * [br.edu.fatec.tcc.guaranin.controller.GlobalAPIExceptionHandler], que trata
 * as exceções padrão do sistema.
 *
 * @see UsuarioAPIController
 * @see br.edu.fatec.tcc.guaranin.controller.GlobalAPIExceptionHandler
 */
@RestControllerAdvice(assignableTypes = [UsuarioAPIController::class])
@Order(Ordered.HIGHEST_PRECEDENCE)
class UsuarioAPIExceptionHandler {

    /**
     * Converte [UsuarioNotFoundException] em `404 Not Found`.
     *
     * @return [ApiErrorDTO] com a mensagem "Usuário não encontrado".
     */
    @ExceptionHandler(UsuarioNotFoundException::class)
    fun handleUsuarioNotFound(): ResponseEntity<ApiErrorDTO> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiErrorDTO("Usuário não encontrado"))
    }
}
