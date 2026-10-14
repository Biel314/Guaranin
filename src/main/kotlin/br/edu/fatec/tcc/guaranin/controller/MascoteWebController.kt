package br.edu.fatec.tcc.guaranin.controller

import br.edu.fatec.tcc.guaranin.dto.MascoteCreateDTO
import br.edu.fatec.tcc.guaranin.dto.MascoteUpdateDTO
import br.edu.fatec.tcc.guaranin.service.MascoteService
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
@RequestMapping("/mascote")
class MascoteWebController(
    private val mascoteService: MascoteService
) {

    @GetMapping("", "/")
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
     * Abre o formulário para criar um novo mascote
     * associado a um usuário.
     */
    @GetMapping("/usuario/{usuarioId}/novo")
    fun novoFormPorUsuario(
        @PathVariable usuarioId: UUID,
        model: Model
    ): String {

        model.addAttribute(
            "mascote",
            MascoteCreateDTO("")
        )

        model.addAttribute(
            "usuarioId",
            usuarioId
        )

        println("CHEGOU NO GET DE CRIACAO")
        println("USUARIO = $usuarioId")

        return "mascote/novo"
    }

    /**
     * Edita o mascote pertencente ao usuário.
     */
    @GetMapping("/usuario/{usuarioId}/editar")
    fun editarPorUsuario(
        @PathVariable usuarioId: UUID,
        model: Model
    ): String {

        val mascote =
            mascoteService.findByUsuarioIdForUpdate(usuarioId)

        model.addAttribute(
            "mascoteUpdateDTO",
            mascote
        )

        return "mascote/editar"
    }

    /**
     * Salva um novo mascote.
     */
    @PostMapping("/usuario/{usuarioId}/salvar")
    fun salvar(
        @PathVariable usuarioId: UUID,
        @Valid @ModelAttribute("mascote") dto: MascoteCreateDTO,
        result: BindingResult,
        model: Model
    ): String {

        if (result.hasErrors()) {

            model.addAttribute(
                "usuarioId",
                usuarioId
            )

            model.addAttribute(
                "errors",
                result.allErrors
            )

            println("CHEGOU NO POST DE CRIACAO")
            println("USUARIO = $usuarioId")

            return "mascote/novo"
        }

        mascoteService.create(
            dto,
            usuarioId
        )

        return "redirect:/usuario/$usuarioId"
    }

    @GetMapping("editar/{id}")
    fun editar(
        @PathVariable id: UUID,
        model: Model
    ): String {

        val mascote =
            mascoteService.findByIdForUpdate(id)

        model.addAttribute(
            "mascoteUpdateDTO",
            mascote
        )

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

    @DeleteMapping("deletar/{id}")
    fun deletar(
        @PathVariable id: UUID
    ): String {

        mascoteService.delete(id)

        return "redirect:/mascote"
    }
}
