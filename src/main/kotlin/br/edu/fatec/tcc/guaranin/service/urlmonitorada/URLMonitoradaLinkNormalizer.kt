package br.edu.fatec.tcc.guaranin.service.urlmonitorada

import java.net.URI
import java.util.Locale

/**
 * Normaliza links de [br.edu.fatec.tcc.guaranin.model.URLMonitorada] para que
 * variações equivalentes sejam tratadas como o mesmo endereço.
 *
 * A normalização remove diferenças de caixa em esquema e host, portas padrão e
 * a barra final do caminho raiz. Caminho, query e fragmento são preservados.
 */
object URLMonitoradaLinkNormalizer {

    private val defaultPorts = mapOf(
        "http" to 80,
        "https" to 443,
        "ftp" to 21
    )

    /**
     * Normaliza o link informado.
     *
     * @param link link recebido da API ou do formulário.
     * @return link normalizado; o valor apenas com espaços removidos quando não
     * for possível interpretar esquema ou host.
     */
    fun normalize(link: String): String {
        val value = link.trim()
        val uri = runCatching { URI(value) }.getOrNull() ?: return value
        val scheme = uri.scheme?.lowercase(Locale.ROOT) ?: return value
        val host = uri.host?.lowercase(Locale.ROOT) ?: return value
        val port = uri.port.takeUnless { it == defaultPorts[scheme] } ?: -1
        val path = uri.rawPath.orEmpty().let { if (it == "/") "" else it }

        return buildString {
            append(scheme)
            append("://")
            uri.rawUserInfo?.let { append(it).append('@') }
            append(host)
            if (port != -1) {
                append(':').append(port)
            }
            append(path)
            uri.rawQuery?.let { append('?').append(it) }
            uri.rawFragment?.let { append('#').append(it) }
        }
    }
}
