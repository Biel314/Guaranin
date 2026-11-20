package br.edu.fatec.tcc.guaranin.service.auth

import br.edu.fatec.tcc.guaranin.dto.auth.LoginRequestDTO
import br.edu.fatec.tcc.guaranin.dto.auth.LoginResponseDTO
import br.edu.fatec.tcc.guaranin.exception.security.CredenciaisInvalidasException
import br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioNotFoundException
import br.edu.fatec.tcc.guaranin.repository.usuario.UsuarioRepository
import br.edu.fatec.tcc.guaranin.security.JwtService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val usuarioRepository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {
    fun login(
        dto: LoginRequestDTO
    ): LoginResponseDTO {
        val usuario = usuarioRepository
            .findByEmail(dto.email)
            .orElseThrow {
                UsuarioNotFoundException()
            }

        if (
            !passwordEncoder.matches(
                dto.password,
                usuario.password
            )
        ) {
            throw CredenciaisInvalidasException()
        }

        return LoginResponseDTO(
            token = jwtService.gerarToken(usuario)
        )
    }
}