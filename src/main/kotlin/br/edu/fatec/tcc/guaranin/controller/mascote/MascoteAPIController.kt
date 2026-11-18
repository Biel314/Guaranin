package br.edu.fatec.tcc.guaranin.controller.mascote

import br.edu.fatec.tcc.guaranin.dto.ApiErrorDTO
import br.edu.fatec.tcc.guaranin.dto.mascote.MascoteCreateDTO
import br.edu.fatec.tcc.guaranin.dto.mascote.MascoteResponseDTO
import br.edu.fatec.tcc.guaranin.dto.mascote.MascoteUpdateDTO
import br.edu.fatec.tcc.guaranin.service.mascote.MascoteService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Controller REST para gerenciamento de
 * [br.edu.fatec.tcc.guaranin.model.Mascote].
 *
 * Expõe operações CRUD em `/api/v1/mascotes` e delega as regras de negócio
 * ao [MascoteService].
 */
@RestController
@RequestMapping("/api/v1/mascotes")
@Tag(name = "Mascotes", description = "Operações REST para gerenciamento de Mascotes")
class MascoteAPIController (
    private val mascoteService: MascoteService
) {
    /**
     * Busca um Mascote pelo identificador.
     *
     * @param mascoteId identificador único do Mascote.
     * @return dados do Mascote encontrado.
     * @throws br.edu.fatec.tcc.guaranin.exception.mascote.MascoteNotFoundException
     * quando o Mascote não existe.
     */
    @GetMapping("/{mascoteId}")
    @Operation(
        summary = "Buscar mascote por ID",
        description = "Retorna os dados de um mascote específico"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Mascote encontrado com sucesso",
                content = [Content(schema = Schema(implementation = MascoteResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "ID do Mascote inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Mascote Não encontrado",
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
        @PathVariable mascoteId: UUID
    ): MascoteResponseDTO {

        return mascoteService.findById(mascoteId)
    }

    /**
     * Busca um Mascote pelo nome informado, ou busca todos os Mascotes
     *
     * O nome é normalizado antes da consulta pelo [MascoteService].
     *
     * @param nome do Mascote.
     * @return dados do Mascote encontrado.
     * @throws br.edu.fatec.tcc.guaranin.exception.mascote.MascoteNotFoundException
     * quando o Mascote não existe.
     */
    @GetMapping("")
    @Operation(
        summary = "Listar mascotes do usuário",
        description = "Retorna uma página de mascotes pertencentes ao usuário"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Mascote encontrado com sucesso",
                content = [Content(schema = Schema(implementation = MascoteResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Mascote não encontrado",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Erro interno",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            )
        ]
    )
    fun listarPorUsuario(
        @ParameterObject pageable: Pageable,
        nome: String?
    ): Page<MascoteResponseDTO> {

        if (nome != null) {
            return mascoteService.findAllByUsuarioIdAndNomeContainingIgnoreCase( nome, pageable)
        }
        else {
            return mascoteService.findAllByUsuarioId(pageable)
        }
    }

    /**
     * Cria um Mascote.
     * @param dto dados do Mascote a ser criada.
     * @return resposta `201 Created` com a Mascote persistido.
     */
    @PostMapping("")
    @Operation(
        summary = "Criar Mascote",
        description = "Cria mascote e retorna 201 com os dados."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Mascote criado com sucesso",
                content = [Content(schema = Schema(implementation = MascoteResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Dados inválidos",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "409",
                description = "Usuario já possui um mascote vivo",
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
        @Valid @RequestBody dto: MascoteCreateDTO
    ): ResponseEntity<MascoteResponseDTO> {

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                mascoteService.create(
                    dto
                )
            )
    }

    /**
     * Atualiza parcialmente um Mascote.
     *
     * Campos nulos no DTO preservam os valores atuais da entidade.
     *
     * @param mascoteId identificador único do Mascote.
     * @param dto dados a serem atualizados.
     * @return dados da Mascote após a atualização.
     * @throws br.edu.fatec.tcc.guaranin.exception.mascote.MascoteNotFoundException
     * quando o Mascote não existe.
     */
    @PutMapping("/{mascoteId}")
    @Operation(
        summary = "Atualizar Mascote",
        description = "Atualizar Mascote"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Mascote atualizado com sucesso",
                content = [Content(schema = Schema(implementation = MascoteResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Dados inválidos ou ID inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Mascote Não encontrado",
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
            description = "Id do Mascote",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable mascoteId: UUID,
        @Valid @RequestBody dto: MascoteUpdateDTO
    ): MascoteResponseDTO {
        return mascoteService.update(mascoteId, dto)
    }

    /**
     * Remove um Mascote.
     *
     * @param mascoteID identificador único do Mascote.
     * @throws br.edu.fatec.tcc.guaranin.exception.mascote.MascoteNotFoundException
     * quando o Mascote não existe.
     */
    @DeleteMapping("/{mascoteId}")
    @Operation(
        summary = "Remover Mascote",
        description = "Remover Mascote e retorna 204 sem corpo"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "Mascote removido com sucesso",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Id do Mascote inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ), ApiResponse(
                responseCode = "404",
                description = "Mascote não encontrado",
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
            description = "Id do Mascote",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable mascoteId: UUID
    ): ResponseEntity<Void> {
        mascoteService.delete(mascoteId)
        return ResponseEntity.noContent().build()
    }
}