package br.edu.fatec.tcc.guaranin.validation.usuario

import br.edu.fatec.tcc.guaranin.validation.RegexPatterns
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 * Validador da restrição [ValidEmail].
 *
 * Valores nulos ou vazios são aceitos, seguindo o comportamento do
 * `@Email` padrão. Valores preenchidos devem corresponder integralmente ao
 * padrão [br.edu.fatec.tcc.guaranin.validation.RegexPatterns.EMAIL].
 *
 * @see ValidEmail
 * @see br.edu.fatec.tcc.guaranin.validation.RegexPatterns.EMAIL
 */
class EmailValidator : ConstraintValidator<ValidEmail, String> {

    private val regex = Regex(RegexPatterns.EMAIL)

    /**
     * Verifica se o e-mail atende ao padrão configurado.
     *
     * @param value e-mail recebido; `null` ou vazio é aceito.
     * @param context contexto da validação, não utilizado por esta regra.
     * @return `true` quando o e-mail é válido; `false` caso contrário.
     */
    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?
    ): Boolean {
        return value == null || value.isEmpty() || regex.matches(value)
    }
}
