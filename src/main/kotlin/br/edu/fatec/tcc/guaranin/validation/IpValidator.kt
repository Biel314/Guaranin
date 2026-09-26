package br.edu.fatec.tcc.guaranin.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.apache.commons.validator.routines.InetAddressValidator

class IpValidator : ConstraintValidator<ValidIp, String> {

    private val validator = InetAddressValidator.getInstance()

    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext
    ): Boolean {
        return value.isNullOrBlank() || validator.isValid(value)
    }
}