package br.edu.fatec.tcc.guaranin.validation.urlmonitorada

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

/**
 * Anotação de validação para endereços IP opcionais.
 *
 * Aceita `null` e valores em branco; quando preenchido, exige IPv4 ou IPv6
 * válido conforme [IpValidator].
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
@Constraint(validatedBy = [IpValidator::class])
annotation class ValidIp(
    val message: String = "Endereço IP inválido",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)