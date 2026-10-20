package br.edu.fatec.tcc.guaranin.controller.usuario

import br.edu.fatec.tcc.guaranin.dto.ApiErrorDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioCreateDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioResponseDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioUpdateDTO
import br.edu.fatec.tcc.guaranin.service.usuario.UsuarioService
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
 * Controller REST responsável pelo CRUD de usuários.
 *
 * Expõe os endpoints sob `/api/usuarios`, recebe e retorna JSON e delega as
 * regras de negócio ao [UsuarioService]. Respostas de erro usam [ApiErrorDTO]
 * e são produzidas por [UsuarioAPIExceptionHandler].
 *
 * @see UsuarioService
 * @see UsuarioAPIExceptionHandler
 */
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "Operações REST para gerenciamento de usuários.")
class UsuarioAPIController(
    private val usuarioService: UsuarioService,
) {

    /**
     * Lista os usuários cadastrados de forma paginada.
     *
     * A paginação e a ordenação são recebidas via [Pageable] e repassadas ao
     * [UsuarioService]. Retorna `200 OK` com uma página de [UsuarioResponseDTO].
     *
     * @param pageable parâmetros de paginação e ordenação.
     * @return página com os usuários encontrados.
     */
    @GetMapping("")
    @Operation(
        summary = "Listar usuários",
        description = "Retorna uma página de usuários com paginação e ordenação."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Página de usuários retornada com sucesso"),
            ApiResponse(
                responseCode = "500",
                description = "Erro interno",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            )
        ]
    )
    fun listarTodos(@ParameterObject pageable: Pageable): Page<UsuarioResponseDTO> {
        return usuarioService.findAll(pageable)
    }

    /**
     * Busca um usuário pelo identificador.
     *
     * Retorna `200 OK` com os dados públicos do usuário ou `404 Not Found`
     * quando o registro não existe.
     *
     * @param id identificador único ([UUID]) do usuário.
     * @return dados públicos do usuário encontrado.
     * @throws br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioNotFoundException se o
     *   usuário não existir; convertida em `404` pelo [UsuarioAPIExceptionHandler].
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar usuário por ID",
        description = "Retorna os dados públicos do usuário."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Usuário encontrado com sucesso",
                content = [Content(schema = Schema(implementation = UsuarioResponseDTO::class))]
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
    fun buscarPorId(
        @Parameter(
            description = "ID do usuário",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable id: UUID
    ): UsuarioResponseDTO {
        return usuarioService.findById(id)
    }

    /**
     * Cria um usuário.
     *
     * Valida o corpo com Bean Validation, delega a criação ao [UsuarioService]
     * e retorna `201 Created` com [UsuarioResponseDTO]. A senha informada não é
     * devolvida na resposta.
     *
     * @param dto dados de criação do usuário.
     * @return resposta `201 Created` com os dados públicos do usuário criado.
     */
    @PostMapping("")
    @Operation(
        summary = "Criar usuário",
        description = "Cria um usuário e retorna 201 com os dados públicos. A senha não é retornada."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Usuário criado com sucesso",
                content = [Content(schema = Schema(implementation = UsuarioResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Dados inválidos",
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
        @Valid @RequestBody dto: UsuarioCreateDTO
    ): ResponseEntity<UsuarioResponseDTO> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(usuarioService.create(dto))
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * O `id` deve ser informado no corpo. A senha é opcional e só é considerada
     * quando preenchida. Retorna `200 OK` com [UsuarioResponseDTO].
     *
     * @param dto dados de atualização do usuário.
     * @return dados públicos do usuário atualizado.
     * @throws br.edu.fatec.tcc.guaranin.exception.InvalidDTOException se o `id`
     *   não for informado; convertida em `400` pelo [UsuarioAPIExceptionHandler].
     * @throws br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioNotFoundException se o
     *   usuário não existir; convertida em `404` pelo [UsuarioAPIExceptionHandler].
     */
    @PutMapping("")
    @Operation(
        summary = "Atualizar usuário",
        description = "Atualiza os dados do usuário. A senha é opcional."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Usuário atualizado com sucesso",
                content = [Content(schema = Schema(implementation = UsuarioResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Dados inválidos ou ID ausente",
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
    fun atualizar(
        @Valid @RequestBody dto: UsuarioUpdateDTO
    ): UsuarioResponseDTO {
        return usuarioService.update(dto)
    }

    /**
     * Remove um usuário pelo identificador.
     *
     * Retorna `204 No Content` sem corpo após a remoção.
     *
     * @param id identificador único ([UUID]) do usuário.
     * @throws br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioNotFoundException se o
     *   usuário não existir; convertida em `404` pelo [UsuarioAPIExceptionHandler].
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Remover usuário",
        description = "Remove o usuário e retorna 204 sem corpo."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Usuário removido com sucesso"),
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
    fun deletar(
        @Parameter(
            description = "ID do usuário",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable id: UUID
    ): ResponseEntity<Void> {
        usuarioService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
