package br.edu.fatec.tcc.guaranin.validation.usuario

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

/**
 * Restrição Bean Validation para senhas de usuário.
 *
 * A validação é delegada a [SenhaValidator], que aplica o padrão
 * [br.edu.fatec.tcc.guaranin.validation.RegexPatterns.SENHA]: de 8 a 72
 * caracteres, sem espaços, com maiúscula, minúscula, número e caractere
 * especial. Valores nulos são aceitos para permitir senhas opcionais em
 * atualizações; a obrigatoriedade deve ser declarada separadamente com
 * `@NotBlank`. Pode ser aplicada em campos, getters e parâmetros de métodos.
 *
 * Em beans de serviço, a anotação só é processada quando a classe possui
 * [org.springframework.validation.annotation.Validated].
 *
 * @property message mensagem retornada quando a senha é inválida.
 * @property groups grupos de validação aos quais a restrição pertence.
 * @property payload metadados adicionais consumidos por validadores.
 * @see SenhaValidator
 * @see br.edu.fatec.tcc.guaranin.validation.RegexPatterns.SENHA
 */
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [SenhaValidator::class])
annotation class ValidSenha(
    val message: String =
        "A senha deve ter entre 8 e 72 caracteres e conter ao menos uma letra maiúscula, uma minúscula, um número e um caractere especial",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
