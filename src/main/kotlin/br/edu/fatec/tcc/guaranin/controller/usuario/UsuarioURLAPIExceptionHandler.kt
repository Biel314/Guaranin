package br.edu.fatec.tcc.guaranin.controller.usuario

import br.edu.fatec.tcc.guaranin.dto.ApiErrorDTO
import br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioURLNotFoundException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(assignableTypes = [UsuarioURLAPIController::class])
@Order(Ordered.HIGHEST_PRECEDENCE)
class UsuarioURLAPIExceptionHandler {
    @ExceptionHandler(UsuarioURLNotFoundException::class)
    fun handleUsuarioURLNotFoundException(ex: UsuarioURLNotFoundException): ResponseEntity<ApiErrorDTO> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiErrorDTO(ex.localizedMessage))
    }
}