package br.edu.fatec.tcc.guaranin.controller.urlmonitorada

import br.edu.fatec.tcc.guaranin.dto.ApiErrorDTO
import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLMonitoradaCreateDTO
import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLMonitoradaResponseDTO
import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLMonitoradaUpdateDTO
import br.edu.fatec.tcc.guaranin.service.urlmonitorada.URLMonitoradaService
import br.edu.fatec.tcc.guaranin.validation.urlmonitorada.ValidUrl
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
 * [br.edu.fatec.tcc.guaranin.model.URLMonitorada].
 *
 * Expõe operações CRUD em `/api/urlmonitorada` e delega as regras de negócio
 * ao [URLMonitoradaService].
 */
@RestController
@RequestMapping("/api/urlmonitorada")
@Tag(name = "URL Monitorada", description = "Operações REST para gerenciamento de URLMonitorada")
class URLMonitoradaAPIController(
    private val urlMonitoradaService: URLMonitoradaService
) {
    /**
     * Lista URLs monitoradas de forma paginada.
     *
     * @param pageable parâmetros de paginação e ordenação.
     * @return página de URLs monitoradas.
     */
    @GetMapping("")
    @Operation(
        summary = "Listar URL monitoradas",
        description = "Retorna uma página de URL monitorada com paginação e ordenação"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Página de URLs monitorada retornada com sucesso"
            ),
            ApiResponse(
                responseCode = "500",
                description = "Erro Interno",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            )
        ]
    )
    fun listarTodos(@ParameterObject pageable: Pageable): Page<URLMonitoradaResponseDTO> {
        return urlMonitoradaService.findAll(pageable)
    }

    /**
     * Busca uma URL monitorada pelo identificador.
     *
     * @param id identificador único da URL monitorada.
     * @return dados da URL monitorada encontrada.
     * @throws br.edu.fatec.tcc.guaranin.exception.urlmonitorada.URLMonitoradaNotFoundException
     * quando a URL não existe.
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar URL monitorada por ID",
        description = "Retorna os dados da URL monitorada"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "URL monitorada encontrada com sucesso",
                content = [Content(schema = Schema(implementation = URLMonitoradaResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "ID da URL monitorada inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "URL Monitorada Não encontrada",
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
            description = "ID da URL monitorada",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable id: UUID
    ): URLMonitoradaResponseDTO {
        return urlMonitoradaService.findById(id)
    }

    /**
     * Busca uma URL monitorada pelo link informado.
     *
     * O link é normalizado antes da consulta pelo [URLMonitoradaService].
     *
     * @param link endereço da URL monitorada.
     * @return dados da URL monitorada encontrada.
     * @throws br.edu.fatec.tcc.guaranin.exception.urlmonitorada.URLMonitoradaNotFoundException
     * quando a URL não existe.
     */
    @GetMapping("/link")
    @Operation(
        summary = "Buscar URL monitorada por URL",
        description = "Retorna os dados da URL monitorada"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "URL monitorada encontrada com sucesso",
                content = [Content(schema = Schema(implementation = URLMonitoradaResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Link da URL monitorada inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "URL Monitorada Não encontrada",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Erro interno",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            )
        ]
    )
    fun buscarPorLink(
        @ValidUrl
        @NotBlank
        @Parameter(
            description = "Link da URLMonitorada",
            example = "https://exemplo.com/"
        )
        @RequestParam link: String
    ): URLMonitoradaResponseDTO {
        return urlMonitoradaService.findByLink(link)
    }

    /**
     * Cria uma URL monitorada.
     *
     * @param dto dados da URL a ser criada.
     * @return resposta `201 Created` com a URL persistida.
     */
    @PostMapping("")
    @Operation(
        summary = "Criar URL monitorada",
        description = "Cria uma URL monitorada e retorna 201 com os dados."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "URL monitorada criada com sucesso",
                content = [Content(schema = Schema(implementation = URLMonitoradaResponseDTO::class))]
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
        @Valid @RequestBody dto: URLMonitoradaCreateDTO
    ): ResponseEntity<URLMonitoradaResponseDTO> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(urlMonitoradaService.create(dto))
    }

    /**
     * Atualiza parcialmente uma URL monitorada.
     *
     * Campos nulos no DTO preservam os valores atuais da entidade.
     *
     * @param id identificador único da URL monitorada.
     * @param dto dados a serem atualizados.
     * @return dados da URL monitorada após a atualização.
     * @throws br.edu.fatec.tcc.guaranin.exception.urlmonitorada.URLMonitoradaNotFoundException
     * quando a URL não existe.
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar URL monitorada",
        description = "Atualizar URL monitorada"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "URL monitorada atualizada com sucesso",
                content = [Content(schema = Schema(implementation = URLMonitoradaResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Dados inválidos ou ID inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "URLMonitorada Não encontrada",
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
            description = "Id da URL monitorada",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable id: UUID,
        @Valid @RequestBody dto: URLMonitoradaUpdateDTO
    ): URLMonitoradaResponseDTO {
        return urlMonitoradaService.update(id, dto)
    }

    /**
     * Remove uma URL monitorada.
     *
     * @param id identificador único da URL monitorada.
     * @throws br.edu.fatec.tcc.guaranin.exception.urlmonitorada.URLMonitoradaNotFoundException
     * quando a URL não existe.
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Remover URL monitorada",
        description = "Remover URL monitorada e retorna 204 sem corpo"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "URL monitorada removida com sucesso",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Id da URLMonitorada inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ), ApiResponse(
                responseCode = "404",
                description = "URLMonitorada não encontrada",
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
            description = "Id da URL monitorada",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable id: UUID
    ): ResponseEntity<Void> {
        urlMonitoradaService.delete(id)
        return ResponseEntity.noContent().build()
    }
}