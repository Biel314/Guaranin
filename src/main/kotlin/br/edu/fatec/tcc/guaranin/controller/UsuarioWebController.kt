package br.edu.fatec.tcc.guaranin.controller

import br.edu.fatec.tcc.guaranin.dto.UsuarioCreateDTO
import br.edu.fatec.tcc.guaranin.dto.UsuarioUpdateDTO
import br.edu.fatec.tcc.guaranin.service.UsuarioService
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
 * Controller web para gerenciamento de Usuários ([br.edu.fatec.tcc.guaranin.model.Usuario]).
 * Fornece operações CRUD integradas com Thymeleaf e documentação OpenAPI/Swagger.
 */
@Controller
@RequestMapping("/usuario")
@Tag(name = "Usuário", description = "Endpoints web para gerenciamento de usuários do sistema")
class UsuarioWebController(
    private val usuarioService: UsuarioService,
) {

    /**
     * Lista todos os usuários cadastrados de forma paginada.
     *
     * @param model Modelo Thymeleaf para passagem de atributos à view.
     * @param pageable Parâmetros de paginação e ordenação.
     * @return Nome da view Thymeleaf `usuario/listar`.
     */
    @GetMapping("", "/")
    @Operation(summary = "Listar usuários", description = "Exibe listagem paginada dos usuários cadastrados.")
    @ApiResponse(responseCode = "200", description = "Listagem carregada com sucesso")
    fun listarTodos(
        model: Model,
        pageable: Pageable
    ): String {
        val usuariosPage = usuarioService.findAll(pageable)
        model.addAttribute("usuarios", usuariosPage.content)
        model.addAttribute("page", usuariosPage)
        return "usuario/listar"
    }

    /**
     * Exibe o formulário de cadastro de novo usuário.
     *
     * @param model Modelo Thymeleaf.
     * @return Nome da view Thymeleaf `usuario/novo`.
     */
    @GetMapping("/novo")
    @Operation(summary = "Formulário de novo usuário", description = "Exibe formulário para cadastro de novo usuário.")
    @ApiResponse(responseCode = "200", description = "Formulário exibido com sucesso")
    fun novoForm(model: Model): String {
        if (!model.containsAttribute("usuario")) {
            model.addAttribute("usuario", UsuarioCreateDTO("", "", ""))
        }
        return "usuario/novo"
    }

    /**
     * Exibe o formulário de edição de um usuário existente.
     *
     * @param id Identificador único ([UUID]) do usuário.
     * @param model Modelo Thymeleaf.
     * @return Nome da view Thymeleaf `usuario/editar`.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Formulário de edição de usuário", description = "Busca usuário por ID e exibe formulário de edição.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Formulário carregado com sucesso"),
        ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    ])
    fun editar(
        @Parameter(description = "ID do usuário") @PathVariable id: UUID,
        model: Model
    ): String {
        val usuario = usuarioService.findByIdForUpdate(id)
        model.addAttribute("usuarioUpdateDTO", usuario)
        return "usuario/editar"
    }

    /**
     * Processa a criação de um novo usuário.
     *
     * @param dto DTO contendo os dados de criação ([UsuarioCreateDTO]).
     * @param result Resultado da validação bean validation.
     * @param model Modelo Thymeleaf.
     * @return Redirecionamento para listagem ou retorno ao formulário em caso de erro.
     */
    @PostMapping("")
    @Operation(summary = "Salvar novo usuário", description = "Valida e persiste novo usuário.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "302", description = "Usuário criado com sucesso, redireciona para listagem"),
        ApiResponse(responseCode = "200", description = "Erro de validação, retorna ao formulário")
    ])
    fun salvar(
        @Valid @ModelAttribute("usuario") dto: UsuarioCreateDTO,
        result: BindingResult,
        model: Model
    ): String {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.allErrors)
            return "usuario/novo"
        }
        usuarioService.create(dto)
        return "redirect:/usuario"
    }

    /**
     * Processa a atualização de um usuário existente.
     *
     * @param dto DTO contendo os dados de atualização ([UsuarioUpdateDTO]).
     * @param result Resultado da validação bean validation.
     * @param model Modelo Thymeleaf.
     * @return Redirecionamento para listagem ou retorno ao formulário em caso de erro.
     */
    @PutMapping("")
    @Operation(summary = "Atualizar usuário", description = "Valida e atualiza usuário existente.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "302", description = "Usuário atualizado com sucesso, redireciona para listagem"),
        ApiResponse(responseCode = "200", description = "Erro de validação, retorna ao formulário")
    ])
    fun atualizar(
        @Valid @ModelAttribute("usuarioUpdateDTO") dto: UsuarioUpdateDTO,
        result: BindingResult,
        model: Model
    ): String {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.allErrors)
            return "usuario/editar"
        }
        usuarioService.update(dto)
        return "redirect:/usuario"
    }

    /**
     * Remove um usuário pelo ID.
     *
     * @param id Identificador único ([UUID]) do usuário.
     * @return Redirecionamento para a listagem.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar usuário", description = "Remove usuário do sistema.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "302", description = "Usuário removido com sucesso, redireciona para listagem"),
        ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    ])
    fun deletar(
        @Parameter(description = "ID do usuário") @PathVariable id: UUID
    ): String {
        usuarioService.delete(id)
        return "redirect:/usuario"
    }
}
