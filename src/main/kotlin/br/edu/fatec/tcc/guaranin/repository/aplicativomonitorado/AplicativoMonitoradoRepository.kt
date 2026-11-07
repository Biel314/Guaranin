package br.edu.fatec.tcc.guaranin.repository.aplicativomonitorado

import br.edu.fatec.tcc.guaranin.model.AplicativoMonitorado
import br.edu.fatec.tcc.guaranin.model.URLHistorico
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

/**
 * Repositório responsável pelo acesso e persistência das entidades
 * [AplicativoMonitorado].
 *
 * Estende [JpaRepository] para operações CRUD padrão e disponibiliza
 * consultas derivadas para recuperação de aplicativos monitorados por
 * pacote, nome e classificação de aplicativo padrão do sistema.
 *
 * Os métodos de busca por nome permitem tanto pesquisas exatas quanto
 * pesquisas parciais ignorando diferenças entre letras maiúsculas e
 * minúsculas.
 *
 * @see AplicativoMonitorado
 */
interface AplicativoMonitoradoRepository : JpaRepository<AplicativoMonitorado, UUID> {

    /**
     * Busca um aplicativo monitorado pelo nome do pacote.
     *
     * O pacote deve corresponder exatamente ao valor armazenado na base
     * de dados.
     *
     * @param pacote identificador único do pacote do aplicativo.
     * @return [Optional] contendo o aplicativo encontrado ou vazio caso
     * não exista correspondência.
     */
    fun findByPacote(
        pacote: String
    ): Optional<AplicativoMonitorado>

    /**
     * Busca um aplicativo monitorado pelo nome.
     *
     * A busca é realizada por correspondência exata.
     *
     * @param nome nome do aplicativo monitorado.
     * @return [Optional] contendo o aplicativo encontrado ou vazio caso
     * não exista correspondência.
     */
    fun findByNome(
        nome: String
    ): Optional<AplicativoMonitorado>

    /**
     * Busca aplicativos monitorados cujo nome contenha o texto informado.
     *
     * A consulta ignora diferenças entre letras maiúsculas e minúsculas.
     *
     * @param nome trecho do nome utilizado como critério de busca.
     * @return lista contendo os aplicativos encontrados.
     */
    fun findByNomeContainingIgnoreCase(
        nome: String
    ): List<AplicativoMonitorado>

    /**
     * Busca aplicativos monitorados cujo nome contenha o texto informado.
     *
     * A consulta ignora diferenças entre letras maiúsculas e minúsculas
     *
     * @param nome nome do aplicativo.
     * @param pageable parâmetros de paginação.
     * @return [Page] contendo os aplicativos encontra.
     */
    fun findAllByNomeContainingIgnoreCase(nome: String, pageable: Pageable): Page<URLHistorico>

    /**
     * Busca todos os aplicativos marcados como padrão.
     *
     * Aplicativos padrão normalmente representam aplicativos nativos
     * do sistema operacional ou previamente cadastrados pela aplicação.
     *
     * @return lista contendo os aplicativos padrão encontrados.
     */
    fun findByPadraoTrue(): List<AplicativoMonitorado>

    /**
     * Busca todos os aplicativos que não estão marcados como padrão.
     *
     * @return lista contendo os aplicativos não padrão encontrados.
     */
    fun findByPadraoFalse(): List<AplicativoMonitorado>
}