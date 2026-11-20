package br.edu.fatec.tcc.guaranin.dto.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginRequestDTO(

    @field:Email
    @field:NotBlank
    val email: String,

    @field:NotBlank
    val password: String
)