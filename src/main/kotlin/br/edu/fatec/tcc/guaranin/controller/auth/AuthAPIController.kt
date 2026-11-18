package br.edu.fatec.tcc.guaranin.controller.auth

import br.edu.fatec.tcc.guaranin.dto.auth.LoginRequestDTO
import br.edu.fatec.tcc.guaranin.dto.auth.LoginResponseDTO
import br.edu.fatec.tcc.guaranin.service.auth.AuthService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthAPIController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(
        @Valid
        @RequestBody dto: LoginRequestDTO
    ): LoginResponseDTO {
        return authService.login(dto)
    }
}