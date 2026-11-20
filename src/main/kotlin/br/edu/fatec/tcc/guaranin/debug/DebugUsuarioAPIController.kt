package br.edu.fatec.tcc.guaranin.debug

import br.edu.fatec.tcc.guaranin.controller.usuario.UsuarioAPIExceptionHandler
import br.edu.fatec.tcc.guaranin.dto.ApiErrorDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioResponseDTO
import br.edu.fatec.tcc.guaranin.legacy.LegacyUsuarioService
import br.edu.fatec.tcc.guaranin.service.usuario.UsuarioService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuários Debug", description = "Operações REST para debug de usuários.")
class DebugUsuarioAPIController(
    val usuarioService: LegacyUsuarioService
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
    @GetMapping("/list")
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
}