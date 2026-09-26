package br.edu.fatec.tcc.guaranin.repository

import br.edu.fatec.tcc.guaranin.model.URLMonitorada
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repositório responsável pelo acesso e persistência das entidades [URLMonitorada].
 *
 * Estende [JpaRepository], disponibilizando operações padrão de CRUD,
 * paginação, ordenação e consulta por identificador.
 *
 * As consultas personalizadas permitem localizar uma URL monitorada
 * pelo endereço da URL ou pelo endereço IP associado.
 */
@Repository
interface URLMonitoradaRepository : JpaRepository<URLMonitorada, Long> {

    /**
     * Busca uma URL monitorada pelo endereço da URL.
     *
     * @param url endereço da URL que será utilizado como critério de busca.
     * @return [Optional] contendo a entidade encontrada ou vazio caso
     * nenhuma URL monitorada corresponda ao endereço informado.
     */
    fun findByUrl(url: String): Optional<URLMonitorada>

    /**
     * Busca uma URL monitorada pelo endereço IP.
     *
     * @param ip endereço IP que será utilizado como critério de busca.
     * @return [Optional] contendo a entidade encontrada ou vazio caso
     * nenhum registro corresponda ao endereço IP informado.
     */
    fun findByIpAddress(ip: String): Optional<URLMonitorada>
}
