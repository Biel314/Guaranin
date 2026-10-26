package br.edu.fatec.tcc.guaranin.validation.usuario

import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioCreateDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioUpdateDTO
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Testes de Bean Validation dos DTOs de usuário.
 *
 * Verifica as restrições padrão e customizadas de e-mail e senha, incluindo
 * a senha opcional na atualização.
 */
class UsuarioValidationTest {

    private lateinit var validator: Validator

    /**
     * Cria o validador Jakarta Bean Validation antes de cada teste.
     */
    @BeforeEach
    fun setup() {
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    /**
     * Verifica se um DTO de criação com dados válidos não possui violações.
     */
    @Test
    fun `deve aceitar usuario valido para criacao`() {
        val dto = UsuarioCreateDTO(
            apelido = "usuario",
            email = "usuario@fatec.br",
            password = "Usuario@123"
        )

        assertTrue(
            validator.validate(dto).isEmpty(),
            "Usuário válido não deveria gerar violações"
        )
    }

    /**
     * Verifica se um e-mail inválido é rejeitado com a mensagem padronizada.
     */
    @Test
    fun `deve rejeitar email invalido`() {
        val dto = UsuarioCreateDTO(
            apelido = "usuario",
            email = "invalid-email",
            password = "Usuario@123"
        )

        assertTrue(
            validator.validate(dto).any { it.message == "Formato de e-mail inválido" },
            "E-mail inválido deveria ser rejeitado"
        )
    }

    /**
     * Verifica se uma senha fraca é rejeitada.
     */
    @Test
    fun `deve rejeitar senha fraca`() {
        val dto = UsuarioCreateDTO(
            apelido = "usuario",
            email = "usuario@fatec.br",
            password = "123"
        )

        assertFalse(
            validator.validate(dto).isEmpty(),
            "Senha fraca deveria ser rejeitada"
        )
    }

    /**
     * Verifica se a senha nula é aceita na atualização, pois é opcional.
     */
    @Test
    fun `deve aceitar senha nula na atualizacao`() {
        val dto = UsuarioUpdateDTO(
            apelido = "usuario",
            email = "usuario@fatec.br",
            password = null
        )

        assertTrue(
            validator.validate(dto).isEmpty(),
            "Senha nula não deveria gerar violações na atualização"
        )
    }

    /**
     * Verifica se uma senha vazia é rejeitada na atualização.
     */
    @Test
    fun `deve rejeitar senha vazia na atualizacao`() {
        val dto = UsuarioUpdateDTO(
            apelido = "usuario",
            email = "usuario@fatec.br",
            password = ""
        )

        assertFalse(
            validator.validate(dto).isEmpty(),
            "Senha vazia deveria ser rejeitada na atualização"
        )
    }
}
