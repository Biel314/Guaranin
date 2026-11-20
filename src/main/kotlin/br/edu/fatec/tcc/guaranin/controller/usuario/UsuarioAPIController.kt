package br.edu.fatec.tcc.guaranin.controller.usuario

import br.edu.fatec.tcc.guaranin.dto.ApiErrorDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioCreateDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioResponseDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioUpdateDTO
import br.edu.fatec.tcc.guaranin.service.usuario.UsuarioService
import br.edu.fatec.tcc.guaranin.validation.usuario.ValidApelido
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
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
 * @property usuarioService service de regras de negócio de usuários.
 * @see UsuarioService
 * @see UsuarioAPIExceptionHandler
 */
@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuários", description = "Operações REST para gerenciamento de usuários.")
class UsuarioAPIController(
    private val usuarioService: UsuarioService,
) {

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
    @PostMapping("/new")
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
     * O `id` deve ser informado no caminho da requisição. A senha é opcional
     * e só é considerada quando preenchida. Retorna `200 OK` com
     * [UsuarioResponseDTO].
     *
     * @param dto dados de atualização do usuário.
     * @return dados públicos do usuário atualizado.
     * @throws br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioNotFoundException se o
     *   usuário não existir; convertida em `404` pelo [UsuarioAPIExceptionHandler].
     */
    @PutMapping("", "/")
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
                description = "Dados inválidos",
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
        @Parameter(
            description = "ID do usuário",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @Valid @RequestBody dto: UsuarioUpdateDTO
    ): UsuarioResponseDTO {
        return usuarioService.update(dto)
    }

    /**
     * Atualiza apenas o apelido de um usuário existente.
     *
     * O apelido é validado por [ValidApelido] antes de ser repassado ao
     * [UsuarioService]. Retorna `200 OK` com os dados públicos atualizados.
     *
     * @param id identificador único ([UUID]) do usuário.
     * @param apelido novo apelido, com 3 a 26 caracteres.
     * @return dados públicos do usuário após a atualização.
     * @throws br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioNotFoundException
     * quando o usuário não existe; convertida em `404` pelo
     * [UsuarioAPIExceptionHandler].
     */
    @PatchMapping("/{id}/apelido/{apelido}")
    @Operation(
        summary = "Atualizar apelido do usuário",
        description = "Atualiza o apelido do usuário."
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
                description = "Apelido inválido",
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
    fun atualizarApelido(
        @Parameter(
            description = "ID do usuário",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable id: UUID,
        @Parameter(
            description = "Apelido do usuário",
            example = "Apelido"
        )
        @ValidApelido
        @PathVariable apelido: String
    ): UsuarioResponseDTO {
        return usuarioService.updateApelido(apelido)
    }

}
