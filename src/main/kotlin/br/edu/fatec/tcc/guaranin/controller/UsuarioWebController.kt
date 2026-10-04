package br.edu.fatec.tcc.guaranin.controller

import br.edu.fatec.tcc.guaranin.dto.UsuarioCreateDTO
import br.edu.fatec.tcc.guaranin.dto.UsuarioUpdateDTO
import br.edu.fatec.tcc.guaranin.service.UsuarioService
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
@RequestMapping("/usuario")
class UsuarioWebController(
    private val usuarioService: UsuarioService,
) {

    @GetMapping("", "/")
    fun listarTodos(model: Model, pageable: Pageable): String {
        val usuariosPage = usuarioService.findAll(pageable)
        model.addAttribute("usuarios", usuariosPage.content)
        model.addAttribute("page", usuariosPage)
        return "usuario/listar"
    }

    @GetMapping("/novo")
    fun novoForm(model: Model): String {
        if (!model.containsAttribute("usuario")) {
            model.addAttribute("usuario", UsuarioCreateDTO("", "", ""))
        }
        return "usuario/novo"
    }

    @GetMapping("/{id}")
    fun editar(@PathVariable id: UUID, model: Model): String {
        val usuario = usuarioService.findByIdForUpdate(id)
        model.addAttribute("usuarioUpdateDTO", usuario)
        return "usuario/editar"
    }

    @PostMapping("")
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

    @PutMapping("")
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

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: UUID): String {
        usuarioService.delete(id)
        return "redirect:/usuario"
    }
}
