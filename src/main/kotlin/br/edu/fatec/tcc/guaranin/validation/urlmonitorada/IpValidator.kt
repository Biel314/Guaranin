package br.edu.fatec.tcc.guaranin.validation.urlmonitorada

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.apache.commons.validator.routines.InetAddressValidator

/**
 * Valida endereços IP usando [InetAddressValidator].
 *
 * O campo é opcional: valores `null` ou em branco são considerados válidos.
 * Quando preenchido, aceita endereços IPv4 e IPv6 válidos.
 */
class IpValidator : ConstraintValidator<ValidIp, String> {

    private val validator = InetAddressValidator.getInstance()

    /**
     * Verifica se o valor é vazio ou um endereço IP válido.
     *
     * @param value valor a ser validado.
     * @param context contexto da validação.
     * @return `true` para valor nulo/branco ou IP válido; `false` caso contrário.
     */
    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext
    ): Boolean {
        return value.isNullOrBlank() || validator.isValid(value)
    }
}