package br.edu.fatec.tcc.guaranin.validation.usuario

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

/**
 * Restrição Bean Validation para e-mails de usuário.
 *
 * A validação é delegada a [EmailValidator], que aplica o padrão
 * [br.edu.fatec.tcc.guaranin.validation.RegexPatterns.EMAIL]. Valores nulos
 * ou vazios são aceitos; a obrigatoriedade deve ser declarada separadamente
 * com `@NotBlank`. Pode ser aplicada em campos, getters e parâmetros de
 * métodos.
 *
 * Em beans de serviço, a anotação só é processada quando a classe possui
 * [org.springframework.validation.annotation.Validated].
 *
 * @property message mensagem retornada quando o e-mail é inválido.
 * @property groups grupos de validação aos quais a restrição pertence.
 * @property payload metadados adicionais consumidos por validadores.
 * @see EmailValidator
 * @see br.edu.fatec.tcc.guaranin.validation.RegexPatterns.EMAIL
 */
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [EmailValidator::class])
annotation class ValidEmail(
    val message: String = "Formato de e-mail inválido",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
