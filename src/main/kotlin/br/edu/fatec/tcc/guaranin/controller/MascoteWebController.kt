package br.edu.fatec.tcc.guaranin.controller

import br.edu.fatec.tcc.guaranin.dto.MascoteCreateDTO
import br.edu.fatec.tcc.guaranin.dto.MascoteUpdateDTO
import br.edu.fatec.tcc.guaranin.service.MascoteService
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
 * Controller web para gerenciamento de Mascotes ([br.edu.fatec.tcc.guaranin.model.Mascote]).
 * Fornece operações CRUD integradas com Thymeleaf e documentação OpenAPI/Swagger.
 */
@Controller
@RequestMapping("/mascote")
@Tag(name = "Mascote", description = "Endpoints web para gerenciamento de mascotes do sistema")
class MascoteWebController (
    private val mascoteService: MascoteService
) {

    /**
     * Lista todos os mascotes cadastrados de forma paginada.
     *
     * @param model Modelo Thymeleaf para passagem de atributos à view.
     * @param pageable Parâmetros de paginação e ordenação.
     * @return Nome da view Thymeleaf `usuario/listar`.
     */
    @GetMapping("", "/")
    @Operation(summary = "Listar mascotes", description = "Exibe listagem paginada dos mascotes cadastrados.")
    @ApiResponse(responseCode = "200", description = "Listagem carregada com sucesso")
    fun listarTodos(
        model: Model,
        pageable: Pageable
    ): String {
        val mascotesPage = mascoteService.findAll(pageable)
        model.addAttribute("mascotes", mascotesPage.content)
        model.addAttribute("page", mascotesPage)
        return "mascote/listar"
    }

    /**
     * Exibe o formulário de cadastro de novo mascote.
     *
     * @param model Modelo Thymeleaf.
     * @return Nome da view Thymeleaf `usuario/novo`.
     */
    @GetMapping("/novo")
    @Operation(summary = "Formulário de novo mascote", description = "Exibe formulário para cadastro de novo mascote.")
    @ApiResponse(responseCode = "200", description = "Formulário exibido com sucesso")
    fun novoForm(model: Model): String {
        if (!model.containsAttribute("mascote")) {
            model.addAttribute("mascote", MascoteCreateDTO(""))
        }
        return "mascote/novo"
    }

    /**
     * Exibe o formulário de edição de um mascote existente.
     *
     * @param id Identificador único ([UUID]) do mascote.
     * @param model Modelo Thymeleaf.
     * @return Nome da view Thymeleaf `usuario/editar`.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Formulário de edição de mascote", description = "Busca mascote por ID e exibe formulário de edição.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Formulário carregado com sucesso"),
        ApiResponse(responseCode = "404", description = "Mascote não encontrado")
    ])
    fun editar(
        @Parameter(description = "ID do mascote") @PathVariable id: UUID,
        model: Model
    ): String {
        val mascote = mascoteService.findByIdForUpdate(id)
        model.addAttribute("mascoteUpdateDTO", mascote)
        return "mascote/editar"
    }

    /**
     * Processa a criação de um novo mascote.
     *
     * @param dto DTO contendo os dados de criação ([MascoteCreateDTO]).
     * @param result Resultado da validação bean validation.
     * @param model Modelo Thymeleaf.
     * @return Redirecionamento para listagem ou retorno ao formulário em caso de erro.
     */
    @PostMapping("")
    @Operation(summary = "Salvar novo mascote", description = "Valida e persiste novo mascote.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "302", description = "Mascote criado com sucesso, redireciona para listagem"),
        ApiResponse(responseCode = "200", description = "Erro de validação, retorna ao formulário")
    ])
    fun salvar(
        @Valid @ModelAttribute("mascote") dto: MascoteCreateDTO,
        result: BindingResult,
        model: Model
    ): String {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.allErrors)
            return "mascote/novo"
        }
        mascoteService.create(dto)
        return "redirect:/mascote"
    }

    /**
     * Processa a atualização de um mascote existente.
     *
     * @param dto DTO contendo os dados de atualização ([MascoteUpdateDTO]).
     * @param result Resultado da validação bean validation.
     * @param model Modelo Thymeleaf.
     * @return Redirecionamento para listagem ou retorno ao formulário em caso de erro.
     */
    @PutMapping("")
    @Operation(summary = "Atualizar mascote", description = "Valida e atualiza mascote existente.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "302", description = "Mascote atualizado com sucesso, redireciona para listagem"),
        ApiResponse(responseCode = "200", description = "Erro de validação, retorna ao formulário")
    ])
    fun atualizar(
        @Valid @ModelAttribute("mascoteUpdateDTO") dto: MascoteUpdateDTO,
        result: BindingResult,
        model: Model
    ): String {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.allErrors)
            return "mascote/editar"
        }
        mascoteService.update(dto)
        return "redirect:/mascote"
    }

    /**
     * Remove um mascote pelo ID.
     *
     * @param id Identificador único ([UUID]) do mascote.
     * @return Redirecionamento para a listagem.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar mascote", description = "Remove mascote do sistema.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "302", description = "Mascote removido com sucesso, redireciona para listagem"),
        ApiResponse(responseCode = "404", description = "Mascote não encontrado")
    ])
    fun deletar(
        @Parameter(description = "ID do mascote") @PathVariable id: UUID
    ): String {
        mascoteService.delete(id)
        return "redirect:/mascote"
    }
}