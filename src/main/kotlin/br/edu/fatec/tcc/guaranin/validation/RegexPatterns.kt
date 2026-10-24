package br.edu.fatec.tcc.guaranin.validation

/**
 * Padrões regulares reutilizáveis pelas validações de entrada da aplicação.
 *
 * As constantes são `const val` para poderem ser usadas diretamente nos
 * argumentos `regexp` de anotações Jakarta Bean Validation, como `@Pattern`
 * e `@Email`.
 *
 * Os padrões priorizam os casos comuns de cadastro; o padrão de e-mail não
 * cobre toda a RFC 5322. A validação de e-mail também aplica as regras
 * nativas de `@Email`.
 *
 * @see jakarta.validation.constraints.Pattern
 * @see jakarta.validation.constraints.Email
 */
object RegexPatterns {

    /**
     * Apelido de usuário.
     *
     * Aceita letras ASCII, números, ponto, sublinhado e hífen, com 3 a 26
     * caracteres.
     */
    const val USUARIO = "^[A-Za-z0-9._-]{3,26}$"

    /**
     * Endereço de e-mail.
     *
     * A parte local é formada por átomos separados por ponto; o domínio exige
     * rótulos válidos e um TLD alfabético de 2 a 63 caracteres.
     */
    const val EMAIL =
        "^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+" +
            "(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*" +
            "@(?:[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?\\.)+" +
            "[A-Za-z]{2,63}$"

    /**
     * Senha de acesso.
     *
     * Exige de 8 a 72 caracteres sem espaços em branco, com pelo menos uma
     * letra minúscula, uma maiúscula, um número e um caractere de pontuação
     * ou símbolo.
     */
    const val SENHA =
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)" +
            "(?=.*[\\p{P}\\p{S}])\\S{8,72}$"
}
