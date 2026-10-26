package br.edu.fatec.tcc.guaranin.service.urlmonitorada

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Testes unitários de [URLMonitoradaLinkNormalizer].
 */
class URLMonitoradaLinkNormalizerTest {

    @Test
    fun `deve normalizar esquema e host para minusculo`() {
        assertEquals(
            "https://example.com/Path?Q=1#Frag",
            URLMonitoradaLinkNormalizer.normalize("  HTTPS://EXAMPLE.COM/Path?Q=1#Frag  ")
        )
    }

    @Test
    fun `deve remover portas padrao`() {
        assertEquals(
            "http://example.com",
            URLMonitoradaLinkNormalizer.normalize("http://example.com:80/")
        )
        assertEquals(
            "https://example.com",
            URLMonitoradaLinkNormalizer.normalize("https://example.com:443/")
        )
        assertEquals(
            "ftp://example.com",
            URLMonitoradaLinkNormalizer.normalize("ftp://example.com:21/")
        )
    }

    @Test
    fun `deve manter porta nao padrao`() {
        assertEquals(
            "https://example.com:8443/api",
            URLMonitoradaLinkNormalizer.normalize("https://example.com:8443/api")
        )
    }

    @Test
    fun `deve remover barra final apenas do caminho raiz`() {
        assertEquals(
            "https://example.com",
            URLMonitoradaLinkNormalizer.normalize("https://example.com/")
        )
        assertEquals(
            "https://example.com?x=1",
            URLMonitoradaLinkNormalizer.normalize("https://example.com/?x=1")
        )
        assertEquals(
            "https://example.com/path/",
            URLMonitoradaLinkNormalizer.normalize("https://example.com/path/")
        )
    }

    @Test
    fun `deve preservar user info e endereco ipv6`() {
        assertEquals(
            "http://user:pass@[::1]/path",
            URLMonitoradaLinkNormalizer.normalize("http://user:pass@[::1]:80/path")
        )
    }

    @Test
    fun `deve ser idempotente`() {
        val normalized = URLMonitoradaLinkNormalizer.normalize("HTTPS://EXAMPLE.COM:443/Path?x=1")
        assertEquals(normalized, URLMonitoradaLinkNormalizer.normalize(normalized))
    }
}
