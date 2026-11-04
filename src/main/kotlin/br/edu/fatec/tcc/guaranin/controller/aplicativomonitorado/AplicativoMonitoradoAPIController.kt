package br.edu.fatec.tcc.guaranin.controller.aplicativomonitorado

import br.edu.fatec.tcc.guaranin.dto.ApiErrorDTO
import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoMonitoradoCreateDTO
import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoMonitoradoResponseDTO
import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoMonitoradoUpdateDTO
import br.edu.fatec.tcc.guaranin.service.aplicativomonitorado.AplicativoMonitoradoService
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
 * [br.edu.fatec.tcc.guaranin.model.AplicativoMonitorado].
 *
 * Expõe operações CRUD em `/api/aplicativomonitorado` e delega as regras de negócio
 * ao [AplicativoMonitoradoService].
 */
@RestController
@RequestMapping("/api/aplicativomonitorado")
@Tag(name = "Aplicativo Monitorado", description = "Operações REST para gerenciamento de AplicativoMonitorado")
class AplicativoMonitoradoAPIController (
    private val aplicativoMonitoradoService: AplicativoMonitoradoService
) {
    /**
     * Lista Aplicativos monitorados de forma paginada.
     *
     * @param pageable parâmetros de paginação e ordenação.
     * @return página de Aplicativos monitorados.
     */
    @GetMapping("")
    @Operation(
        summary = "Listar Aplicativos monitorados",
        description = "Retorna uma página de Aplicativos monitorados com paginação e ordenação"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Página de Aplicativos Monitorados retornada com sucesso"
            ),
            ApiResponse(
                responseCode = "500",
                description = "Erro Interno",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            )
        ]
    )
    fun listarTodos(@ParameterObject pageable: Pageable): Page<AplicativoMonitoradoResponseDTO> {
        return aplicativoMonitoradoService.findAll(pageable)
    }

    /**
     * Busca um AplicativoMonitorado pelo identificador.
     *
     * @param id identificador único do AplicativoMonitorado.
     * @return dados do AplicativoMonitorado encontrado.
     * @throws br.edu.fatec.tcc.guaranin.exception.aplicativomonitorado.AplicativoMonitoradoNotFoundException
     * quando o Aplicativo não existe.
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar Aplicativo monitorado por ID",
        description = "Retorna os dados do Aplicativo monitorado"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Aplicativo monitorado encontrado com sucesso",
                content = [Content(schema = Schema(implementation = AplicativoMonitoradoResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "ID do Aplicativo monitorado inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Aplicativo monitorado Não encontrada",
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
            description = "ID do Aplicativo Monitorado",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable id: UUID
    ): AplicativoMonitoradoResponseDTO {
        return aplicativoMonitoradoService.findById(id)
    }

    /**
     * Busca um Aplicativo monitorado pelo nome informado.
     *
     * O nome é normalizado antes da consulta pelo [AplicativoMonitoradoService].
     *
     * @param nome do Aplicativo monitorado.
     * @return dados do Aplicativo monitorado encontrado.
     * @throws br.edu.fatec.tcc.guaranin.exception.aplicativomonitorado.AplicativoMonitoradoNotFoundException
     * quando o Aplicativo não existe.
     */
    @GetMapping("/nome")
    @Operation(
        summary = "Buscar Aplicativo monitorada por nome",
        description = "Retorna os dados do Aplicativo Monitorado"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Aplicativo monitorado encontrado com sucesso",
                content = [Content(schema = Schema(implementation = AplicativoMonitoradoResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Nome do Aplicativo monitorado inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Aplicativo Monitorado não encontrado",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Erro interno",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            )
        ]
    )
    fun buscarPorNome(
        @NotBlank
        @Parameter(
            description = "Nome do AplicativoMonitorado",
            example = "Instagram"
        )
        @RequestParam nome: String
    ): AplicativoMonitoradoResponseDTO {
        return aplicativoMonitoradoService.findByNome(nome)
    }

    /**
     * Cria um AplicativoMonitorado.
     *
     * @param dto dados do Aplicativo a ser criada.
     * @return resposta `201 Created` com a Aplicativo persistido.
     */
    @PostMapping("")
    @Operation(
        summary = "Criar Aplicativo monitorado",
        description = "Cria uma Aplicativo monitorado e retorna 201 com os dados."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Aplicativo monitorado criado com sucesso",
                content = [Content(schema = Schema(implementation = AplicativoMonitoradoResponseDTO::class))]
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
        @Valid @RequestBody dto: AplicativoMonitoradoCreateDTO
    ): ResponseEntity<AplicativoMonitoradoResponseDTO> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(aplicativoMonitoradoService.create(dto))
    }

    /**
     * Atualiza parcialmente um AplicativoMonitorado.
     *
     * Campos nulos no DTO preservam os valores atuais da entidade.
     *
     * @param id identificador único do Aplicativo Monitorado.
     * @param dto dados a serem atualizados.
     * @return dados da Aplicativo Monitorado após a atualização.
     * @throws br.edu.fatec.tcc.guaranin.exception.aplicativomonitorado.AplicativoMonitoradoNotFoundException
     * quando o Aplicativo Monitorado não existe.
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar Aplicativo monitorado",
        description = "Atualizar Aplicativo monitorado"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Aplicativo monitorado atualizado com sucesso",
                content = [Content(schema = Schema(implementation = AplicativoMonitoradoResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Dados inválidos ou ID inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "AplicativoMonitorado Não encontrado",
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
            description = "Id do Aplicativo Monitorado",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable id: UUID,
        @Valid @RequestBody dto: AplicativoMonitoradoUpdateDTO
    ): AplicativoMonitoradoResponseDTO {
        return aplicativoMonitoradoService.update(id, dto)
    }

    /**
     * Remove um AplicativoMonitorado.
     *
     * @param id identificador único do AplicativoMonitorado.
     * @throws br.edu.fatec.tcc.guaranin.exception.aplicativomonitorado.AplicativoMonitoradoNotFoundException
     * quando o AplicativoMonitorado não existe.
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Remover Aplicativo monitorado",
        description = "Remover Aplicativo monitorado e retorna 204 sem corpo"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "Aplicativo monitorado removido com sucesso",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Id do AplicativoMonitorado inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ), ApiResponse(
                responseCode = "404",
                description = "AplicativoMonitorado não encontrado",
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
            description = "Id do AplicativoMonitorado",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable id: UUID
    ): ResponseEntity<Void> {
        aplicativoMonitoradoService.delete(id)
        return ResponseEntity.noContent().build()
    }
}