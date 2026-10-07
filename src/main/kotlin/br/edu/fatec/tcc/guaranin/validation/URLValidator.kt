package br.edu.fatec.tcc.guaranin.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.apache.commons.validator.routines.UrlValidator

class URLValidator : ConstraintValidator<ValidUrl, String> {

    private val validator = UrlValidator.getInstance()

    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext
    ): Boolean {
        return value.isNullOrBlank() || validator.isValid(value)
    }
}
