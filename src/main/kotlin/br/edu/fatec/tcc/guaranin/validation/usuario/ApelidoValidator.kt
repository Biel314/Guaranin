package br.edu.fatec.tcc.guaranin.validation.usuario

import br.edu.fatec.tcc.guaranin.validation.RegexPatterns
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 * Validador da restrição [ValidApelido].
 *
 * Considera válido apenas um valor não nulo, não em branco e que corresponda
 * integralmente ao padrão
 * [br.edu.fatec.tcc.guaranin.validation.RegexPatterns.USUARIO].
 *
 * @see ValidApelido
 * @see br.edu.fatec.tcc.guaranin.validation.RegexPatterns.USUARIO
 */
class ApelidoValidator : ConstraintValidator<ValidApelido, String> {

    private val regex = Regex(RegexPatterns.USUARIO)

    /**
     * Verifica se o apelido atende ao padrão configurado.
     *
     * @param value apelido recebido; `null` ou em branco é inválido.
     * @param context contexto da validação, não utilizado por esta regra.
     * @return `true` quando o apelido é válido; `false` caso contrário.
     */
    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?
    ): Boolean {
        return !value.isNullOrBlank() && regex.matches(value)
    }
}
