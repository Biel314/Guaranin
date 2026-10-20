package br.edu.fatec.tcc.guaranin.validation.urlmonitorada

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

/**
 * Anotação de validação para URLs.
 *
 * Aceita `null` para campos opcionais, mas rejeita valores em branco ou com
 * formato inválido conforme [URLValidator].
 *
 * @property message mensagem exibida quando o valor é inválido.
 * @property groups grupos de validação aos quais a restrição pertence.
 * @property payload metadados adicionais da restrição.
 */
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
