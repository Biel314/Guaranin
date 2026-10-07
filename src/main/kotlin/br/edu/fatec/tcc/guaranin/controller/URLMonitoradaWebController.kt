package br.edu.fatec.tcc.guaranin.controller

import br.edu.fatec.tcc.guaranin.dto.URLMonitoradaCreateDTO
import br.edu.fatec.tcc.guaranin.dto.URLMonitoradaUpdateDTO
import br.edu.fatec.tcc.guaranin.mapper.URLMonitoradaMapper
import br.edu.fatec.tcc.guaranin.service.URLMonitoradaService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Controller web para gerenciamento de URLs Monitoradas ([br.edu.fatec.tcc.guaranin.model.URLMonitorada]).
 * Fornece endpoints CRUD integrados com Thymeleaf e documentados via Swagger/OpenAPI.
 */
@Controller
@RequestMapping("/url-monitorado")
@Tag(name = "URL Monitorado", description = "Endpoints web para gerenciamento e cadastro de URLs monitoradas")
class URLMonitoradaWebController(
    private val urlMonitoradaService: URLMonitoradaService,
    private val urlMonitoradaMapper: URLMonitoradaMapper
) {

    /**
     * Lista todas as URLs monitoradas de forma paginada.
     *
     * @param model Modelo Thymeleaf para passagem de atributos à view.
     * @param pageable Parâmetros de paginação e ordenação.
     * @return Nome da view Thymeleaf `url-monitorado/listar`.
     */
    @GetMapping("", "/")
    @Operation(summary = "Listar URLs monitoradas", description = "Exibe listagem paginada das URLs monitoradas.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Listagem carregada com sucesso")
    ])
    fun listarTodos(
        model: Model,
        pageable: Pageable
    ): String {
        val page = urlMonitoradaService.findAll(pageable)
        model.addAttribute("urls", page.content)
        model.addAttribute("page", page)
        return "url-monitorado/listar"
    }

    /**
     * Exibe o formulário de cadastro de nova URL monitorada.
     *
     * @param model Modelo Thymeleaf.
     * @return Nome da view Thymeleaf `url-monitorado/novo`.
     */
    @GetMapping("/novo")
    @Operation(summary = "Formulário de nova URL", description = "Exibe formulário para cadastro de nova URL monitorada.")
    @ApiResponse(responseCode = "200", description = "Formulário exibido com sucesso")
    fun novoForm(model: Model): String {
        if (!model.containsAttribute("urlMonitorada")) {
            model.addAttribute("urlMonitorada", URLMonitoradaCreateDTO("", null, false))
        }
        return "url-monitorado/novo"
    }

    /**
     * Exibe o formulário de edição de uma URL monitorada existente.
     *
     * @param id Identificador único ([UUID]) da URL.
     * @param model Modelo Thymeleaf.
     * @return Nome da view Thymeleaf `url-monitorado/editar`.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Formulário de edição", description = "Busca URL por ID e exibe formulário de edição.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Formulário de edição carregado com sucesso"),
        ApiResponse(responseCode = "404", description = "URL monitorada não encontrada")
    ])
    fun editar(
        @Parameter(description = "ID da URL monitorada") @PathVariable id: UUID,
        model: Model
    ): String {
        val responseDTO = urlMonitoradaService.findById(id)
        val updateDTO = URLMonitoradaUpdateDTO(
            id = responseDTO.id,
            link = responseDTO.link,
            ip = responseDTO.ip,
            padrao = responseDTO.padrao
        )
        model.addAttribute("urlMonitoradaUpdateDTO", updateDTO)
        return "url-monitorado/editar"
    }

    /**
     * Processa a criação de uma nova URL monitorada.
     *
     * @param dto DTO contendo os dados de criação ([URLMonitoradaCreateDTO]).
     * @param result Resultado da validação bean validation.
     * @param model Modelo Thymeleaf.
     * @return Redirecionamento para listagem ou retorno ao formulário em caso de erro.
     */
    @PostMapping("")
    @Operation(summary = "Salvar nova URL", description = "Valida e persiste nova URL monitorada.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "302", description = "URL criada com sucesso, redireciona para listagem"),
        ApiResponse(responseCode = "200", description = "Erro de validação, retorna ao formulário")
    ])
    fun salvar(
        @Valid @ModelAttribute("urlMonitorada") dto: URLMonitoradaCreateDTO,
        result: BindingResult,
        model: Model
    ): String {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.allErrors)
            return "url-monitorado/novo"
        }
        urlMonitoradaService.create(dto)
        return "redirect:/url-monitorado"
    }

    /**
     * Processa a atualização de uma URL monitorada existente.
     *
     * @param dto DTO contendo os dados de atualização ([URLMonitoradaUpdateDTO]).
     * @param result Resultado da validação bean validation.
     * @param model Modelo Thymeleaf.
     * @return Redirecionamento para listagem ou retorno ao formulário em caso de erro.
     */
    @PutMapping("")
    @Operation(summary = "Atualizar URL", description = "Valida e atualiza URL monitorada existente.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "302", description = "URL atualizada com sucesso, redireciona para listagem"),
        ApiResponse(responseCode = "200", description = "Erro de validação, retorna ao formulário")
    ])
    fun atualizar(
        @Valid @ModelAttribute("urlMonitoradaUpdateDTO") dto: URLMonitoradaUpdateDTO,
        result: BindingResult,
        model: Model
    ): String {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.allErrors)
            return "url-monitorado/editar"
        }
        urlMonitoradaService.update(dto)
        return "redirect:/url-monitorado"
    }

    /**
     * Remove uma URL monitorada pelo ID.
     *
     * @param id Identificador único ([UUID]) da URL.
     * @return Redirecionamento para a listagem.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar URL", description = "Remove URL monitorada do sistema.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "302", description = "URL removida com sucesso, redireciona para listagem"),
        ApiResponse(responseCode = "404", description = "URL monitorada não encontrada")
    ])
    fun deletar(
        @Parameter(description = "ID da URL monitorada") @PathVariable id: UUID
    ): String {
        urlMonitoradaService.delete(id)
        return "redirect:/url-monitorado"
    }
}
