package br.edu.fatec.tcc.guaranin.controller.aplicativomonitorado

import br.edu.fatec.tcc.guaranin.dto.ApiErrorDTO
import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoHistoricoCreateDTO
import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoHistoricoResponseDTO
import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoMonitoradoCreateDTO
import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoMonitoradoResponseDTO
import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoMonitoradoUpdateDTO
import br.edu.fatec.tcc.guaranin.exception.aplicativomonitorado.FiltroHistoricoInvalidoException
import br.edu.fatec.tcc.guaranin.service.aplicativomonitorado.AplicativoHistoricoService
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
@RequestMapping("/api/v1/historicos-aplicativos")
@Tag(name = "Historicos de Uso dos Aplicativos", description = "Operações REST para gerenciamento de AplicatioHistorico")
class AplicativoHistoricoAPIController(
    private val aplicativoHistoricoService: AplicativoHistoricoService
) {

    /**
     * Busca um AplicativoHistorico pelo identificador.
     *
     * @param historicoId identificador único do AplicativoHistorico.
     * @return dados do AplicativoHistorico encontrado.
     * @throws br.edu.fatec.tcc.guaranin.exception.aplicativomonitorado.AplicativoHistoricoNotFoundException
     * quando o Historico não existe.
     */
    @GetMapping("/{historicoId}")
    @Operation(
        summary = "Buscar Historicos por ID",
        description = "Retorna os dados dos Historicos de aplicativo"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Historico encontrado com sucesso",
                content = [Content(schema = Schema(implementation = AplicativoMonitoradoResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "ID do Historico inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Historico Não encontrado",
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
            description = "ID do Historico",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable historicoId: UUID
    ): AplicativoHistoricoResponseDTO {
        return aplicativoHistoricoService.findHistoricoById(historicoId)
    }

    /**
     * Busca um Historico pelo pacote ou nome informado do aplicativo, ou busca todos os historicos
     *
     * O nome é normalizado antes da consulta pelo [AplicativoHistoricoService].
     *
     * @param pacote do Aplicativo monitorado.
     * @param nome do Aplicativo monitorado
     * @return dados do Historico do Aplicativo monitorado encontrado.
     * @throws br.edu.fatec.tcc.guaranin.exception.aplicativomonitorado.AplicativoHistoricoNotFoundException
     * quando o Historico não existe.
     */
    @GetMapping("")
    @Operation(
        summary = "Buscar um Historico de Aplicativo monitorado por nome, ou se vazio, busca todos os historicos",
        description = "Retorna os dados do Historico do Aplicativo Monitorado"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Historicos encontrados com sucesso",
                content = [Content(schema = Schema(implementation = AplicativoHistoricoResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Pacote ou Nome do Aplicativo monitorado inválido",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Informe apenas um filtro por vez (nome ou pacote).",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            )
            ApiResponse(
                responseCode = "404",
                description = "Historicos para Aplicativo Monitorado não encontrado",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Erro interno",
                content = [Content(schema = Schema(implementation = ApiErrorDTO::class))]
            )
        ]
    )
    fun buscar(
        @Parameter(
            description = "Pacote dos Aplicativo Monitorados do Usuario",
            example = "com.meta.instagram"
        )
        @RequestParam(required = false)
        pacote: String?,
        @RequestParam(required = false)
        nome: String?,
        pageable: Pageable
    ): Page<AplicativoHistoricoResponseDTO> {

        if (nome != null && pacote != null) {
            throw FiltroHistoricoInvalidoException()
        }

        return when {
            nome != null ->
                aplicativoHistoricoService
                    .findAllByNomeContainingIgnoreCase(
                        nome,
                        pageable
                    )

            pacote != null ->
                aplicativoHistoricoService
                    .findAllByPacoteIgnoreCase(
                        pacote,
                        pageable
                    )

            else ->
                aplicativoHistoricoService
                    .findAllByUsuarioId(
                        pageable
                    )
        }

    }

    /**
     * Cria um Historico de AplicativoMonitorado.
     *
     * @param dto dados do Historico di Aplicativo a ser criada.
     * @return resposta `201 Created` com a Historico persistido.
     */
    @PostMapping("")
    @Operation(
        summary = "Criar Historico de Aplicativo monitorado",
        description = "Cria um Historico de Aplicativo monitorado e retorna 201 com os dados."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Historico criado com sucesso",
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
        @Valid @RequestBody dto: AplicativoHistoricoCreateDTO
    ): ResponseEntity<AplicativoHistoricoResponseDTO> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(aplicativoHistoricoService.create(dto))
    }


}