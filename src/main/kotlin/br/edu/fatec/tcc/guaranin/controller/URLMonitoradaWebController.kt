package br.edu.fatec.tcc.guaranin.controller

import br.edu.fatec.tcc.guaranin.dto.URLMonitoradaCreateDTO
import br.edu.fatec.tcc.guaranin.dto.URLMonitoradaUpdateDTO
import br.edu.fatec.tcc.guaranin.mapper.URLMonitoradaMapper
import br.edu.fatec.tcc.guaranin.service.URLMonitoradaService
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
@RequestMapping("/url-monitorada")
class URLMonitoradaWebController(
    private val urlMonitoradaService: URLMonitoradaService,
    private val urlMonitoradaMapper: URLMonitoradaMapper
) {

    @GetMapping("", "/")
    fun listarTodos(model: Model, pageable: Pageable): String {
        val page = urlMonitoradaService.findAll(pageable)
        model.addAttribute("urls", page.content)
        model.addAttribute("page", page)
        return "url-monitorada/listar"
    }

    @GetMapping("/novo")
    fun novoForm(model: Model): String {
        if (!model.containsAttribute("urlMonitorada")) {
            model.addAttribute("urlMonitorada", URLMonitoradaCreateDTO("", null, false))
        }
        return "url-monitorada/novo"
    }

    @GetMapping("/{id}")
    fun editar(@PathVariable id: UUID, model: Model): String {
        val responseDTO = urlMonitoradaService.findById(id)
        val updateDTO = URLMonitoradaUpdateDTO(
            id = responseDTO.id,
            link = responseDTO.link,
            ip = responseDTO.ip,
            padrao = responseDTO.padrao
        )
        model.addAttribute("urlMonitoradaUpdateDTO", updateDTO)
        return "url-monitorada/editar"
    }

    @PostMapping("")
    fun salvar(
        @Valid @ModelAttribute("urlMonitorada") dto: URLMonitoradaCreateDTO,
        result: BindingResult,
        model: Model
    ): String {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.allErrors)
            return "url-monitorada/novo"
        }
        urlMonitoradaService.create(dto)
        return "redirect:/url-monitorada"
    }

    @PutMapping("")
    fun atualizar(
        @Valid @ModelAttribute("urlMonitoradaUpdateDTO") dto: URLMonitoradaUpdateDTO,
        result: BindingResult,
        model: Model
    ): String {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.allErrors)
            return "url-monitorada/editar"
        }
        urlMonitoradaService.update(dto)
        return "redirect:/url-monitorada"
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: UUID): String {
        urlMonitoradaService.delete(id)
        return "redirect:/url-monitorada"
    }
}
