package br.edu.fatec.tcc.guaranin.controller.usuario

import br.edu.fatec.tcc.guaranin.dto.ApiErrorDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioURLCreateDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioURLResponseDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioURLUpdateDTO
import br.edu.fatec.tcc.guaranin.service.usuario.UsuarioURLService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Controller REST responsável pelo gerenciamento de associações entre usuários e URLs ([br.edu.fatec.tcc.guaranin.model.UsuarioURL]).
 *
 * Expõe os endpoints sob `/api/usuarios_url`, recebe e retorna JSON e delega as
 * regras de negócio ao [UsuarioURLService]. Respostas de erro usam [ApiErrorDTO]
 * e são produzidas por [UsuarioURLAPIExceptionHandler].
 *
 * @property usuarioURLService serviço de regras de negócio de associações.
 * @see UsuarioURLService
 * @see UsuarioURLAPIExceptionHandler
 */
@RestController
@RequestMapping("/api/usuarios_url")
@Tag(
    name = "Usuários URL",
    description = "Operações REST para gerenciamento de associações entre usuários e URLs monitoradas."
)
class UsuarioURLAPIController(
    private val usuarioURLService: UsuarioURLService
) {

    /**
     * Lista as associações cadastradas de forma paginada.
     *
     * A paginação e a ordenação são recebidas via [Pageable] e repassadas ao
     * [UsuarioURLService]. Retorna `200 OK` com uma página de [UsuarioURLResponseDTO].
     *
     * @param pageable parâmetros de paginação e ordenação.
     * @return página com as associações encontradas.
     */
    @GetMapping("")
    @Operation(
        summary = "Listar associações de usuário e URL",
        description = "Retorna uma página de associações com paginação e ordenação."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Página de associações retornada com sucesso"),
            ApiResponse(
                responseCode = "500",
                description = "Erro interno",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            )
        ]
    )
    fun listarTodos(@ParameterObject pageable: Pageable): Page<UsuarioURLResponseDTO> {
        return usuarioURLService.findAll(pageable)
    }

    /**
     * Lista as associações de um usuário específico de forma paginada.
     *
     * Retorna `200 OK` com uma página de [UsuarioURLResponseDTO] ou `404 Not Found`
     * se o usuário não existir.
     *
     * @param id identificador único ([UUID]) do usuário.
     * @param pageable parâmetros de paginação e ordenação.
     * @return página com as associações encontradas.
     * @throws br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioNotFoundException se o
     *   usuário não existir; convertida em `404` pelo [UsuarioURLAPIExceptionHandler].
     */
    @GetMapping("/usuario/{id}")
    @Operation(
        summary = "Listar associações por ID do usuário",
        description = "Retorna uma página de associações de URLs filtrada pelo ID do usuário."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Página de associações retornada com sucesso",
                content = [Content(schema = Schema(implementation = UsuarioURLResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "ID do usuário inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Usuário não encontrado",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Erro interno",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            )
        ]
    )
    fun listarPorUsuarioId(
        @Parameter(
            description = "ID do Usuário",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable id: UUID,
        @ParameterObject pageable: Pageable
    ): Page<UsuarioURLResponseDTO> {
        return usuarioURLService.findByUsuarioId(id, pageable)
    }

    /**
     * Lista as associações de uma URL monitorada específica de forma paginada.
     *
     * Retorna `200 OK` com uma página de [UsuarioURLResponseDTO] ou `404 Not Found`
     * se a URL monitorada não existir.
     *
     * @param id identificador único ([UUID]) da URL monitorada.
     * @param pageable parâmetros de paginação e ordenação.
     * @return página com as associações encontradas.
     * @throws br.edu.fatec.tcc.guaranin.exception.urlmonitorada.URLMonitoradaNotFoundException se a
     *   URL monitorada não existir; convertida em `404` pelo [UsuarioURLAPIExceptionHandler].
     */
    @GetMapping("/link/{id}")
    @Operation(
        summary = "Listar associações por ID da URL monitorada",
        description = "Retorna uma página de associações de URLs filtrada pelo ID da URL monitorada."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Página de associações retornada com sucesso",
                content = [Content(schema = Schema(implementation = UsuarioURLResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "ID da URL monitorada inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "URL monitorada não encontrada",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Erro interno",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            )
        ]
    )
    fun listarPorURLMonitorada(
        @Parameter(
            description = "ID da URL Monitorada",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable id: UUID,
        @ParameterObject pageable: Pageable
    ): Page<UsuarioURLResponseDTO> {
        return usuarioURLService.findByUrlMonitoradaId(id, pageable)
    }


    /**
     * Busca uma associação pelo identificador.
     *
     * Retorna `200 OK` com os dados da associação ou `404 Not Found`
     * quando o registro não existe.
     *
     * @param id identificador único ([UUID]) da associação.
     * @return dados da associação encontrada.
     * @throws br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioURLNotFoundException se a
     *   associação não existir; convertida em `404` pelo [UsuarioURLAPIExceptionHandler].
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar associação por ID",
        description = "Retorna os dados da associação entre usuário e URL."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Associação encontrada com sucesso",
                content = [Content(schema = Schema(implementation = UsuarioURLResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "ID da associação inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Associação não encontrada",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Erro interno",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            )
        ]
    )
    fun buscarPorId(
        @Parameter(
            description = "ID da associação",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable id: UUID
    ): UsuarioURLResponseDTO {
        return usuarioURLService.findById(id)
    }

    /**
     * Cria uma associação entre usuário e URL monitorada.
     *
     * Valida o corpo com Bean Validation, delega a criação ao [UsuarioURLService]
     * e retorna `201 Created` com [UsuarioURLResponseDTO].
     *
     * @param dto dados de criação da associação.
     * @return resposta `201 Created` com os dados da associação criada.
     * @throws br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioNotFoundException se o usuário não existir.
     * @throws br.edu.fatec.tcc.guaranin.exception.urlmonitorada.URLMonitoradaNotFoundException se a URL não existir.
     */
    @PostMapping("")
    @Operation(
        summary = "Criar associação",
        description = "Cria uma associação entre usuário e URL monitorada e retorna 201."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Associação criada com sucesso",
                content = [Content(schema = Schema(implementation = UsuarioURLResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Dados inválidos",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Usuário ou URL monitorada não encontrados",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Erro interno",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            )
        ]
    )
    fun criar(
        @Valid @RequestBody dto: UsuarioURLCreateDTO
    ): ResponseEntity<UsuarioURLResponseDTO> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(usuarioURLService.create(dto))
    }

    /**
     * Atualiza parcialmente uma associação existente.
     *
     * O `id` do registro é informado no caminho da requisição. Campos nulos no
     * DTO preservam os valores atuais.
     *
     * @param id identificador único ([UUID]) da associação.
     * @param dto dados de atualização ([UsuarioURLUpdateDTO]).
     * @return [UsuarioURLResponseDTO] da associação atualizada.
     * @throws br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioURLNotFoundException se a associação não existir.
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar associação",
        description = "Atualiza parcialmente os dados de uma associação existente."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Associação atualizada com sucesso",
                content = [Content(schema = Schema(implementation = UsuarioURLResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Dados inválidos",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Associação, usuário ou URL não encontrados",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Erro interno",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            )
        ]
    )
    fun atualizar(
        @Parameter(
            description = "ID da associação",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable id: UUID,
        @Valid @RequestBody dto: UsuarioURLUpdateDTO
    ): UsuarioURLResponseDTO {
        return usuarioURLService.update(id, dto)
    }

    /**
     * Remove uma associação pelo identificador.
     *
     * Retorna `204 No Content` sem corpo após a remoção.
     *
     * @param id identificador único ([UUID]) da associação.
     * @throws br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioURLNotFoundException se a
     *   associação não existir; convertida em `404` pelo [UsuarioURLAPIExceptionHandler].
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Remover associação",
        description = "Remove a associação e retorna 204 sem corpo."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Associação removida com sucesso"),
            ApiResponse(
                responseCode = "400",
                description = "ID da associação inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Associação não encontrada",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Erro interno",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            )
        ]
    )
    fun deletar(
        @Parameter(
            description = "ID da associação",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable id: UUID
    ): ResponseEntity<Void> {
        usuarioURLService.delete(id)
        return ResponseEntity.noContent().build()
    }
}