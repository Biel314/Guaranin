package br.edu.fatec.tcc.guaranin.controller.mascote

import br.edu.fatec.tcc.guaranin.dto.mascote.MascoteCreateDTO
import br.edu.fatec.tcc.guaranin.dto.mascote.MascoteUpdateDTO
import br.edu.fatec.tcc.guaranin.service.mascote.MascoteService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

@Controller
@RequestMapping("/mascote")
class MascoteWebController(
    private val mascoteService: MascoteService,
) {

    @GetMapping("", "/debug-todos-mascotes")
    fun listarTodos(
        model: Model,
        pageable: Pageable
    ): String {
        val mascotesPage = mascoteService.findAll(pageable)

        model.addAttribute("mascotes", mascotesPage.content)
        model.addAttribute("page", mascotesPage)

        return "mascote/listar"
    }

    @GetMapping("")
    fun listarPorUsuario(
        model: Model,
        pageable: Pageable
    ): String {
        val mascotePage = mascoteService.findAllByUsuarioId(pageable)

        val possuiMascoteVivo = mascoteService.possuiMascoteVivo()

        model.addAttribute("mascotes", mascotePage.content)

        model.addAttribute("page", mascotePage)

        model.addAttribute("possuiMascoteVivo", possuiMascoteVivo)

        return "mascote/listar"
    }

    @GetMapping("/visualizar/{mascoteId}")
    fun visualizar(
        @PathVariable mascoteId: UUID,
        model: Model
    ): String {
        val mascote = mascoteService.findById(mascoteId)

        model.addAttribute("mascote", mascote)

        return "mascote/visualizar"
    }

    /**
     * Salva um novo mascote.
     */
    @PostMapping("/novo")
    fun novoMascote(
        @Valid @ModelAttribute("mascote") dto: MascoteCreateDTO,
        result: BindingResult,
        model: Model
    ): String {
        println("CHEGOU NO POST DE CRIACAO FORMULARIO")

        if (result.hasErrors()) {


            model.addAttribute(
                "errors",
                result.allErrors
            )


            return "mascote/novo"
        }

        mascoteService.create(
            dto
        )

        return ""
    }

    @GetMapping("/novo")
    fun novoFormulario(
        model: Model
    ): String {
        if (mascoteService.possuiMascoteVivo()) {
            return "redirect:"
        }

        model.addAttribute("mascote", MascoteCreateDTO(""))

        return "mascote/novo"
    }

    @GetMapping("/editar/{mascoteId}")
    fun editar(
        @PathVariable mascoteId: UUID,
        model: Model
    ): String {

        val mascote =
            mascoteService.findByIdForUpdate(mascoteId)

        model.addAttribute("mascoteUpdateDTO", mascote)

        return "mascote/editar"
    }

    @PutMapping("")
    fun atualizar(
        @Valid @ModelAttribute("mascoteUpdateDTO") dto: MascoteUpdateDTO,
        result: BindingResult,
        model: Model
    ): String {

        if (result.hasErrors()) {

            model.addAttribute(
                "errors",
                result.allErrors
            )

            return "mascote/editar"
        }

        mascoteService.update(dto)

        return "redirect:/mascote"
    }

    @PostMapping("/matar/{mascoteId}")
    fun matar(@PathVariable mascoteId: UUID): String {
        mascoteService.matarMascote(mascoteId)
        return "redirect:/mascote/usuario/"
    }

    @DeleteMapping("deletar/{id}")
    fun deletar(
        @PathVariable id: UUID
    ): String {

        mascoteService.delete(id)

        return "redirect:/mascote"
    }
}
