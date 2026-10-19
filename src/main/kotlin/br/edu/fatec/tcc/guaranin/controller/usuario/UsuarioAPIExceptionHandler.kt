package br.edu.fatec.tcc.guaranin.controller.usuario

import br.edu.fatec.tcc.guaranin.dto.ApiErrorDTO
import br.edu.fatec.tcc.guaranin.exception.InvalidDTOException
import br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

/**
 * Tratamento centralizado de exceções do [UsuarioAPIController].
 *
 * Restrito ao controller via `assignableTypes`, converte exceções de domínio,
 * validação e parâmetros em [ApiErrorDTO], mantendo o contrato
 * `{"error": "..."}`. Erros inesperados são registrados no log e retornam
 * `500` sem expor detalhes internos.
 *
 * @see UsuarioAPIController
 * @see ApiErrorDTO
 */
@RestControllerAdvice(assignableTypes = [UsuarioAPIController::class])
class UsuarioAPIExceptionHandler {

    private val log = LoggerFactory.getLogger(UsuarioAPIExceptionHandler::class.java)

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

    /**
     * Converte [InvalidDTOException] em `400 Bad Request`.
     *
     * Usado quando o ID obrigatório não é informado no DTO de atualização.
     *
     * @return [ApiErrorDTO] com a mensagem "DTO inválido ou ID ausente".
     */
    @ExceptionHandler(InvalidDTOException::class)
    fun handleInvalidDTO(): ResponseEntity<ApiErrorDTO> {
        return ResponseEntity.badRequest()
            .body(ApiErrorDTO("DTO inválido ou ID ausente"))
    }

    /**
     * Converte falhas de Bean Validation em `400 Bad Request`.
     *
     * Usa a mensagem do primeiro campo inválido encontrado. Se não houver erro
     * de campo, retorna "Dados inválidos".
     *
     * @param ex exceção com o resultado da validação.
     * @return [ApiErrorDTO] com a mensagem do primeiro campo inválido.
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ApiErrorDTO> {
        val message = ex.bindingResult.fieldErrors.firstOrNull()?.defaultMessage
            ?: "Dados inválidos"
        return ResponseEntity.badRequest().body(ApiErrorDTO(message))
    }

    /**
     * Converte corpos ausentes, malformados ou incompatíveis em
     * `400 Bad Request`.
     *
     * @return [ApiErrorDTO] com a mensagem "Corpo da requisição inválido".
     */
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleMalformedBody(): ResponseEntity<ApiErrorDTO> {
        return ResponseEntity.badRequest()
            .body(ApiErrorDTO("Corpo da requisição inválido"))
    }

    /**
     * Converte parâmetros de rota ou consulta com tipo inválido em
     * `400 Bad Request`.
     *
     * @return [ApiErrorDTO] com a mensagem "Parâmetro inválido".
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleInvalidParameter(): ResponseEntity<ApiErrorDTO> {
        return ResponseEntity.badRequest()
            .body(ApiErrorDTO("Parâmetro inválido"))
    }

    /**
     * Converte exceções não tratadas em `500 Internal Server Error`.
     *
     * O detalhe é registrado no log; a resposta não expõe mensagem interna nem
     * stack trace.
     *
     * @param ex exceção inesperada.
     * @return [ApiErrorDTO] com a mensagem "Erro interno".
     */
    @ExceptionHandler(Exception::class)
    fun handleUnexpected(ex: Exception): ResponseEntity<ApiErrorDTO> {
        log.error("Erro inesperado ao processar requisição da API de usuários", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiErrorDTO("Erro interno"))
    }
}
