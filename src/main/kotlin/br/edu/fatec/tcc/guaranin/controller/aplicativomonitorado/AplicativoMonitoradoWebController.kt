package br.edu.fatec.tcc.guaranin.controller.aplicativomonitorado

import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoMonitoradoCreateDTO
import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoMonitoradoUpdateDTO
import br.edu.fatec.tcc.guaranin.service.aplicativomonitorado.AplicativoMonitoradoService
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
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.util.UUID

@Controller
@RequestMapping("/aplicativo-monitorado")
@Tag(name = "Aplicativo Monitorado", description = "Endpoints web para gerenciamento e cadastro de Aplicativos monitorados")
class AplicativoMonitoradoWebController (
    private val aplicativoMonitoradoService : AplicativoMonitoradoService
)
{
    @GetMapping("/aplicativo-monitorado")
    @Operation(summary = "Listar Aplicativos monitorados")
    @ApiResponses(value = [
            ApiResponse(responseCode = "200", description = "Listagem carregada com sucesso"),
        ])
    fun listarTodos(
        model: Model,
        pageable: Pageable
    ): String {
        val page = aplicativoMonitoradoService.findAll(pageable)
        model.addAttribute("aplicativos", page.content)
        model.addAttribute("page", page)
        return "aplicativo-monitorado/"
    }

    @GetMapping("/novo")
    @Operation(summary = "Formulário novo Aplicativo Monitorado")
    @ApiResponse(responseCode = "200", description = "Formulário exibido com sucesso")
    fun novoForm(
        model: Model
    ): String {
        if (!model.containsAttribute("aplicativoMonitorado")) {
            model.addAttribute("aplicativoMonitorado", AplicativoMonitoradoCreateDTO("", "", false))
        }
        return "aplicativo-monitorado/novo"
    }

    @GetMapping("/{id}")
    @Operation(summary = "Formulário de edição", description = "Busca Aplicativo por ID e exibe formulário de edição.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Formulário de edição carregado com sucesso"),
        ApiResponse(responseCode = "404", description = "Aplicativo monitorado não encontrado")
    ])
    fun editar(
        @Parameter(description = "ID Aplicativo monitorado") @PathVariable id: UUID,
        model: Model
    ): String {
        val responseDTO = aplicativoMonitoradoService.findById(id)
        val updateDTO = AplicativoMonitoradoUpdateDTO(
            pacote = responseDTO.pacote,
            nome = responseDTO.nome,
            padrao = responseDTO.padrao
        )
        model.addAttribute("id", id)
        model.addAttribute("aplicativoMonitoradoUpdateDTO", updateDTO)
        return "aplicativo-monitorado/editar"
    }

    @PostMapping("")
    @Operation(summary = "Salvar novo Aplicativo", description = "Valida e persiste novo Aplicativo monitorado.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "302", description = "Aplicativo criado com sucesso, redirecionando para listagem"),
        ApiResponse(responseCode = "200", description = "Erro de validação, retornando ao formulário")
    ])
    fun salvar(
        @Valid @ModelAttribute("form") dto: AplicativoMonitoradoCreateDTO,
        result: BindingResult,
        model: Model
    ): String {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.allErrors)
            return "aplicativo-monitorado/novo"
        }
        aplicativoMonitoradoService.create(dto)
        return "redirect:/aplicativo-monitorado"
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Aplicativo", description = "Valida e atualiza Aplicativo monitorado existente.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "302", description = "Aplicativo atualizado com sucesso, redirecionando para listagem"),
        ApiResponse(responseCode = "200", description = "Erro de validação, retornando ao formulário")
    ])
    fun atualizar(
        @Parameter(description = "ID Aplicativo monitorado") @PathVariable id: UUID,
        @Valid @ModelAttribute("form") dto: AplicativoMonitoradoUpdateDTO,
        result: BindingResult,
        model: Model
    ): String {
        if (result.hasErrors()) {
            model.addAttribute("id", id)
            model.addAttribute("errors", result.allErrors)
            return "aplicativo-monitorado/editar"
        }
        aplicativoMonitoradoService.update(id, dto)
        return "redirect:/aplicativo-monitorado"
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar Aplicativo", description = "Remove Aplicativo monitorado do sistema.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "302", description = "Aplicativo removido com sucesso, redirecionando para listagem"),
        ApiResponse(responseCode = "404", description = "Aplicativo monitorado não encontrado")
    ])
    fun deletar(
        @Parameter(description = "ID Aplicativo monitorado") @PathVariable id: UUID
    ): String {
        aplicativoMonitoradoService.delete(id)
        return "redirect:/aplicativo-monitorado"
    }

}