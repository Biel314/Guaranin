package br.edu.fatec.tcc.guaranin.validation.urlmonitorada

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.apache.commons.validator.routines.UrlValidator

/**
 * Valida URLs usando [UrlValidator].
 *
 * Valores `null` são aceitos para permitir campos opcionais. Strings em branco
 * ou com formato inválido são rejeitadas.
 */
class URLValidator : ConstraintValidator<ValidUrl, String> {

    private val validator = UrlValidator.getInstance()

    /**
     * Verifica se o valor é nulo ou uma URL válida.
     *
     * @param value valor a ser validado.
     * @param context contexto da validação.
     * @return `true` para valor nulo ou URL válida; `false` caso contrário.
     */
    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext
    ): Boolean {
        return value == null || validator.isValid(value)
    }
}
