package br.edu.fatec.tcc.guaranin.validation.usuario

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

/**
 * Restrição Bean Validation para apelidos de usuário.
 *
 * A validação é delegada a [ApelidoValidator], que aplica o padrão
 * [br.edu.fatec.tcc.guaranin.validation.RegexPatterns.USUARIO]: de 3 a 26
 * caracteres, contendo apenas letras ASCII, números, ponto, sublinhado e
 * hífen. Pode ser aplicada em campos, getters e parâmetros de métodos.
 *
 * Em beans de serviço, a anotação só é processada quando a classe possui
 * [org.springframework.validation.annotation.Validated].
 *
 * @property message mensagem retornada quando o apelido é inválido.
 * @property groups grupos de validação aos quais a restrição pertence.
 * @property payload metadados adicionais consumidos por validadores.
 * @see ApelidoValidator
 * @see br.edu.fatec.tcc.guaranin.validation.RegexPatterns.USUARIO
 */
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ApelidoValidator::class])
annotation class ValidApelido(
    val message: String = "Apelido de Usuário inválido.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
