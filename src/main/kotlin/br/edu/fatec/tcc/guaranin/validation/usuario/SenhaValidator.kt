package br.edu.fatec.tcc.guaranin.validation.usuario

import br.edu.fatec.tcc.guaranin.validation.RegexPatterns
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 * Validador da restrição [ValidSenha].
 *
 * Valores nulos são aceitos para permitir senhas opcionais em atualizações.
 * Quando preenchido, o valor não pode ser em branco e deve corresponder
 * integralmente ao padrão
 * [br.edu.fatec.tcc.guaranin.validation.RegexPatterns.SENHA].
 *
 * @see ValidSenha
 * @see br.edu.fatec.tcc.guaranin.validation.RegexPatterns.SENHA
 */
class SenhaValidator : ConstraintValidator<ValidSenha, String> {

    private val regex = Regex(RegexPatterns.SENHA)

    /**
     * Verifica se a senha atende ao padrão configurado.
     *
     * @param value senha recebida; `null` é aceito, mas vazio ou em branco é inválido.
     * @param context contexto da validação, não utilizado por esta regra.
     * @return `true` quando a senha é válida; `false` caso contrário.
     */
    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?
    ): Boolean {
        return value == null || (!value.isBlank() && regex.matches(value))
    }
}
