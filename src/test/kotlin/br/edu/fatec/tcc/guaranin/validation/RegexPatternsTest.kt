package br.edu.fatec.tcc.guaranin.validation

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Testes unitários dos padrões centralizados em [RegexPatterns].
 *
 * Cobre casos válidos e inválidos de apelido, e-mail e senha, incluindo
 * limites de tamanho e os caracteres especiais aceitos pela política
 * atual.
 */
class RegexPatternsTest {

    /**
     * Verifica se o valor corresponde integralmente ao padrão informado.
     *
     * @param pattern expressão regular a ser aplicada.
     * @param value valor a ser testado.
     * @return `true` se o valor corresponder integralmente ao padrão.
     */
    private fun matches(pattern: String, value: String): Boolean =
        Regex(pattern).matches(value)

    /** Verifica se [RegexPatterns.USUARIO] aceita apelidos válidos. */
    @Test
    fun `deve aceitar apelidos validos`() {
        listOf("testuser", "user.name", "user_name", "user-name", "abc123")
            .forEach { apelido ->
                assertTrue(
                    matches(RegexPatterns.USUARIO, apelido),
                    "Apelido deveria ser válido: $apelido"
                )
            }
    }

    /** Verifica se [RegexPatterns.USUARIO] rejeita apelidos inválidos. */
    @Test
    fun `deve rejeitar apelidos invalidos`() {
        listOf(
            "ab",
            "user name",
            "user@name",
            "usuário",
            "a".repeat(27)
        ).forEach { apelido ->
            assertFalse(
                matches(RegexPatterns.USUARIO, apelido),
                "Apelido deveria ser inválido: $apelido"
            )
        }
    }

    /** Verifica se [RegexPatterns.EMAIL] aceita e-mails válidos. */
    @Test
    fun `deve aceitar emails validos`() {
        listOf(
            "test@fatec.br",
            "nome.sobrenome@fatec.sp.gov.br",
            "user+tag@gmail.com",
            "user-name@example.com"
        ).forEach { email ->
            assertTrue(
                matches(RegexPatterns.EMAIL, email),
                "E-mail deveria ser válido: $email"
            )
        }
    }

    /** Verifica se [RegexPatterns.EMAIL] rejeita e-mails inválidos. */
    @Test
    fun `deve rejeitar emails invalidos`() {
        listOf(
            "invalid-email",
            "user@dominio_1.com",
            "user@dominio.123",
            "a..b@example.com",
            ".a@example.com",
            "a@example..com"
        ).forEach { email ->
            assertFalse(
                matches(RegexPatterns.EMAIL, email),
                "E-mail deveria ser inválido: $email"
            )
        }
    }

    /** Verifica se [RegexPatterns.SENHA] aceita senhas válidas. */
    @Test
    fun `deve aceitar senhas validas`() {
        listOf(
            "Password@123",
            "Pass#word1",
            "Pass_word1",
            "Pass-word1",
            "Pass+word1",
            "Aa1!" + "a".repeat(68)
        ).forEach { senha ->
            assertTrue(
                matches(RegexPatterns.SENHA, senha),
                "Senha deveria ser válida: $senha"
            )
        }
    }

    /** Verifica se [RegexPatterns.SENHA] rejeita senhas inválidas. */
    @Test
    fun `deve rejeitar senhas invalidas`() {
        listOf(
            "123",
            "password",
            "PASSWORD@123",
            "Password123",
            "Pass 1234",
            "Passç1234",
            "Aa1!" + "a".repeat(69)
        ).forEach { senha ->
            assertFalse(
                matches(RegexPatterns.SENHA, senha),
                "Senha deveria ser inválida: $senha"
            )
        }
    }
}
