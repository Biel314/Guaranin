package br.edu.fatec.tcc.guaranin.controller

import br.edu.fatec.tcc.guaranin.dto.ApiErrorDTO
import br.edu.fatec.tcc.guaranin.exception.InvalidDTOException
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

/**
 * Tratamento centralizado das exceções padrão das APIs REST.
 *
 * Restrito aos controllers anotados com [RestController], portanto não afeta
 * os controllers MVC/Thymeleaf. Os handlers de exceções de domínio permanecem
 * nos advices específicos de cada API, que têm precedência sobre este.
 *
 * @see ApiErrorDTO
 */
@RestControllerAdvice(annotations = [RestController::class])
@Order(Ordered.LOWEST_PRECEDENCE)
class GlobalAPIExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalAPIExceptionHandler::class.java)

    /**
     * Converte [InvalidDTOException] em `400 Bad Request`.
     *
     * @return [ApiErrorDTO] com a mensagem "DTO inválido ou ID ausente".
     */
    @ExceptionHandler(InvalidDTOException::class)
    fun handleInvalidDTO(): ResponseEntity<ApiErrorDTO> {
        return ResponseEntity.badRequest()
            .body(ApiErrorDTO("DTO inválido ou ID ausente"))
    }

    /**
     * Converte falhas de Bean Validation do corpo em `400 Bad Request`.
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
     * Converte falhas de Bean Validation de parâmetros de método em
     * `400 Bad Request`.
     *
     * @param ex exceção com os resultados da validação de parâmetros.
     * @return [ApiErrorDTO] com a mensagem do primeiro erro encontrado.
     */
    @ExceptionHandler(HandlerMethodValidationException::class)
    fun handleMethodValidation(ex: HandlerMethodValidationException): ResponseEntity<ApiErrorDTO> {
        val message = ex.allErrors.firstOrNull()?.defaultMessage
            ?: "Parâmetro inválido"
        return ResponseEntity.badRequest().body(ApiErrorDTO(message))
    }

    /**
     * Converte a ausência de parâmetro obrigatório em `400 Bad Request`.
     *
     * @param ex exceção com o nome do parâmetro ausente.
     * @return [ApiErrorDTO] indicando o parâmetro obrigatório.
     */
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingParameter(ex: MissingServletRequestParameterException): ResponseEntity<ApiErrorDTO> {
        return ResponseEntity.badRequest()
            .body(ApiErrorDTO("Parâmetro obrigatório ausente: ${ex.parameterName}"))
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
     * Converte violações de Bean Validation de entidades em `400 Bad Request`.
     *
     * @param ex exceção com as violações encontradas.
     * @return [ApiErrorDTO] com a mensagem da primeira violação.
     */
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<ApiErrorDTO> {
        val message = ex.constraintViolations.firstOrNull()?.message
            ?: "Dados inválidos"
        return ResponseEntity.badRequest().body(ApiErrorDTO(message))
    }

    /**
     * Converte violações de integridade do banco em `409 Conflict`.
     *
     * O detalhe técnico é registrado no log e não é exposto ao cliente.
     *
     * @param ex exceção de integridade lançada pela camada de persistência.
     * @return [ApiErrorDTO] com a mensagem "Conflito de dados: registro já existe".
     */
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolation(ex: DataIntegrityViolationException): ResponseEntity<ApiErrorDTO> {
        log.warn("Conflito de integridade ao processar requisição", ex)
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ApiErrorDTO("Conflito de dados: registro já existe"))
    }

    /**
     * Converte exceções não tratadas em `500 Internal Server Error`.
     *
     * O detalhe é registrado no log; a resposta não expõe mensagem interna nem
     * stack trace.
     *
     * @param ex exceção inesperada.
     * @param request requisição que originou o erro, usada apenas no log.
     * @return [ApiErrorDTO] com a mensagem "Erro interno".
     */
    @ExceptionHandler(Exception::class)
    fun handleUnexpected(ex: Exception, request: HttpServletRequest): ResponseEntity<ApiErrorDTO> {
        log.error("Erro inesperado em {} {}", request.method, request.requestURI, ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiErrorDTO("Erro interno"))
    }
}
