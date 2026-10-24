package br.edu.fatec.tcc.guaranin.controller.urlmonitorada

import br.edu.fatec.tcc.guaranin.dto.ApiErrorDTO
import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLHistoricoCreateDTO
import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLHistoricoResponseDTO
import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLHistoricoUpdateDTO
import br.edu.fatec.tcc.guaranin.service.urlmonitorada.URLHistoricoService
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
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*

/**
 * Controller REST para gerenciamento do histórico de acesso a URLs.
 *
 * Expõe operações CRUD em `/api/urlhistorico`, além de consultas por usuário,
 * associação `UsuarioURL` e data/hora de acesso. Delega as regras de negócio
 * ao [URLHistoricoService].
 *
 * @property urlHistoricoService serviço de regras de negócio do histórico.
 * @see URLHistoricoService
 * @see URLHistoricoAPIExceptionHandler
 */
@RestController
@RequestMapping("/api/urlhistorico")
@Tag(name = "URL Histórico", description = "Operações REST para histórico de acesso a URLs")
class URLHistoricoAPIController(
    private val urlHistoricoService: URLHistoricoService
) {

    /**
     * Lista os históricos de acesso de forma paginada.
     *
     * @param pageable parâmetros de paginação e ordenação.
     * @return página de históricos.
     */
    @GetMapping("")
    @Operation(
        summary = "Listar URL Historicos",
        description = "Retorna uma página de URL historicos com paginação e ordenação"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Página de URL historicos retornada com sucesso",
            ),
            ApiResponse(
                responseCode = "500",
                description = "Erro Interno",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            )
        ]
    )
    fun listarTodos(@ParameterObject pageable: Pageable): Page<URLHistoricoResponseDTO> {
        return urlHistoricoService.findAll(pageable)
    }

    /**
     * Busca um histórico pelo identificador.
     *
     * @param id identificador único do histórico.
     * @return dados do histórico encontrado.
     * @throws br.edu.fatec.tcc.guaranin.exception.urlmonitorada.URLHistoricoNotFoundException
     * quando o histórico não existe.
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar URL Historico por ID",
        description = "Retorna os dados da URL Historico"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "URL Historico encontrada com sucesso",
                content = [Content(schema = Schema(implementation = URLHistoricoResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "ID da URL Historico inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "URL Historico Não encontrada",
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
            description = "ID da URL Historico",
            example = "8f19e7a7-48f6-43e1-b0ba-477afe97f06d",
        )
        @PathVariable id: UUID
    ): URLHistoricoResponseDTO {
        return urlHistoricoService.findById(id)
    }

    /**
     * Busca os históricos de um usuário de forma paginada.
     *
     * @param id identificador único do usuário.
     * @param pageable parâmetros de paginação e ordenação.
     * @return página de históricos do usuário.
     */
    @GetMapping("usuario/{id}")
    @Operation(
        summary = "Buscar página de URL Historico por id de Usuário",
        description = "Retorna página de URL Historico do usuario"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Página de URL Historico retornada com sucesso"
            ),
            ApiResponse(
                responseCode = "400",
                description = "Id do usuário inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "URL Historico Não encontrada",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Erro interno",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            )
        ]
    )
    fun buscarPorUsuario(
        @Parameter(
            description = "ID do usuário",
            example = "8f19e7a7-48f6-43e1-b0ba-477afe97f06d"
        )
        @PathVariable id: UUID,
        @ParameterObject pageable: Pageable
    ): Page<URLHistoricoResponseDTO> {
        return urlHistoricoService.findAllByUsuarioId(id, pageable)
    }

    /**
     * Busca os históricos de uma data e hora de acesso específicas.
     *
     * @param dataHoraAcesso data e hora exata do acesso em formato ISO-8601.
     * @param pageable parâmetros de paginação e ordenação.
     * @return página de históricos encontrados.
     */
    @GetMapping("acesso/{dataHoraAcesso}")
    @Operation(
        summary = "Buscar URL Historico por data e hora de acesso",
        description = "Retorna uma página de URL Historicos com a data e hora de acesso informada"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Página de URL Historico retornada com sucesso"
            ),
            ApiResponse(
                responseCode = "400",
                description = "Data e Hora de Acesso inválida",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "URL Historico Não encontrada",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Erro interno",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            )
        ]
    )
    fun buscarPorDataHoraAcesso(
        @Parameter(
            description = "Data Hora de Acesso em formato ISO-8601",
            example = "2026-10-24T10:30:00"
        )
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @PathVariable dataHoraAcesso: LocalDateTime,
        @ParameterObject pageable: Pageable
    ): Page<URLHistoricoResponseDTO> {
        return urlHistoricoService.findAllByDataHoraAcesso(dataHoraAcesso, pageable)
    }

    /**
     * Busca o histórico associado a uma relação `UsuarioURL`.
     *
     * @param usuarioURLId identificador da associação `UsuarioURL`.
     * @return dados do histórico encontrado.
     * @throws br.edu.fatec.tcc.guaranin.exception.urlmonitorada.URLHistoricoNotFoundException
     * quando não há histórico para a associação informada.
     */
    @GetMapping("/usuario_url/{usuarioURLId}")
    @Operation(
        summary = "Buscar URL Historico por Usuario URL ID",
        description = "Retorna os dados da URL Historico"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "URL Historico encontrada com sucesso",
                content = [Content(schema = Schema(implementation = URLHistoricoResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "ID da Usuario URL inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "URL Historico Não encontrada",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Erro interno",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            )
        ]
    )
    fun buscarPorUsuarioURL(
        @Parameter(
            description = "ID da associação Usuario URL",
            example = "8f19e7a7-48f6-43e1-b0ba-477afe97f06d",
        )
        @PathVariable usuarioURLId: UUID
    ): URLHistoricoResponseDTO {
        return urlHistoricoService.findByUsuarioURL(usuarioURLId)
    }

    /**
     * Cria um registro de histórico de acesso.
     *
     * @param dto dados do histórico a ser criado.
     * @return resposta `201 Created` com o histórico persistido.
     */
    @PostMapping("")
    @Operation(
        summary = "Criar URL Historico",
        description = "Cria uma URL Historico"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "URL Historico criada com sucesso",
                content = [Content(schema = Schema(implementation = URLHistoricoResponseDTO::class))]
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
        @Valid @RequestBody dto: URLHistoricoCreateDTO
    ): ResponseEntity<URLHistoricoResponseDTO> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(urlHistoricoService.create(dto))
    }

    /**
     * Atualiza parcialmente um histórico existente.
     *
     * Campos nulos no DTO preservam os valores atuais da entidade.
     *
     * @param id identificador único do histórico.
     * @param urlHistoricoUpdateDTO dados a serem atualizados.
     * @return dados do histórico após a atualização.
     * @throws br.edu.fatec.tcc.guaranin.exception.urlmonitorada.URLHistoricoNotFoundException
     * quando o histórico não existe.
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar URL Historico",
        description = "Atualizar URL Historico"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "URL Historico atualizada com sucesso",
                content = [Content(schema = Schema(implementation = URLHistoricoResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Dados inválidos ou ID inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "URL Historico Não encontrada",
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
            description = "ID da URL Historico",
            example = "8f19e7a7-48f6-43e1-b0ba-477afe97f06d"
        )
        @PathVariable id: UUID,
        @Valid @RequestBody urlHistoricoUpdateDTO: URLHistoricoUpdateDTO
    ): URLHistoricoResponseDTO {
        return urlHistoricoService.update(id, urlHistoricoUpdateDTO)
    }

    /**
     * Remove um histórico pelo identificador.
     *
     * @param id identificador único do histórico.
     * @throws br.edu.fatec.tcc.guaranin.exception.urlmonitorada.URLHistoricoNotFoundException
     * quando o histórico não existe.
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Remover URL Historico",
        description = "Remover URL Historico"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "URL Historico removida com sucesso",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Id da URL Historico inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ), ApiResponse(
                responseCode = "404",
                description = "URL Historico não encontrada",
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
            description = "ID da URL Historico",
            example = "8f19e7a7-48f6-43e1-b0ba-477afe97f06d"
        )
        @PathVariable id: UUID
    ): ResponseEntity<Void> {
        urlHistoricoService.delete(id)
        return ResponseEntity.noContent().build()
    }
}