package br.edu.fatec.tcc.guaranin.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.*

/**
 * Entidade JPA que representa um endereço sob monitoramento no sistema Guaranin.
 *
 * Cada registro associa um endereço [link] ao endereço [ip] resolvido
 * para ele, permitindo confrontar periodicamente o resultado da resolução
 * DNS com o valor esperado e detectar remanejamentos do endereço.
 *
 * O atributo [link] é único em toda a base, servindo como chave natural de
 * consulta: um mesmo endereço não pode ser monitorado duas vezes, mesmo que
 * por requisições concorrentes.
 *
 * @property id identificador da URL monitorada, gerado automaticamente.
 * @property link endereço sendo monitorado, obrigatório e único na base (máximo 4050 caracteres).
 * @property ip endereço IP associado ao campo [link] (máximo 50 caracteres).
 * @property padrao indentificador se o link é monitorado por decisão do usuário ou padrão da aplicação.
 */
@Entity
@Table(name = "tb_url_monitorado")
class URLMonitorada(
    @Id
    @Column(name = "id_url_monitorado", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @Column(name = "link", nullable = false, length = 4050, unique = true)
    @NotBlank(message = "O link é obrigatório")
    var link: String = "",

    @Column(name = "ip", length = 50)
    @Size(max = 50)
    var ip: String? = null,

    @Column(name = "padrao", nullable = false)
    var padrao: Boolean = false
) {}
