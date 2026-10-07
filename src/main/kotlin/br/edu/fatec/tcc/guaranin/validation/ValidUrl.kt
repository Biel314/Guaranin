package br.edu.fatec.tcc.guaranin.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [URLValidator::class])
annotation class ValidUrl(
    val message: String = "URL inválida",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
