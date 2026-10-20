package br.edu.fatec.tcc.guaranin.service.urlmonitorada

import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLMonitoradaCreateDTO
import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLMonitoradaResponseDTO
import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLMonitoradaUpdateDTO
import br.edu.fatec.tcc.guaranin.mapper.urlmonitorada.URLMonitoradaMapper
import br.edu.fatec.tcc.guaranin.model.URLMonitorada
import br.edu.fatec.tcc.guaranin.repository.urlmonitorada.URLMonitoradaRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import java.util.*

/**
 * Testes unitários de [URLMonitoradaService], com repositório e mapper mockados.
 */
class URLMonitoradaServiceTest {

    private val urlMonitoradaRepository = mock(URLMonitoradaRepository::class.java)
    private val urlMonitoradaMapper = mock(URLMonitoradaMapper::class.java)
    private val service = URLMonitoradaService(urlMonitoradaRepository, urlMonitoradaMapper)

    @Suppress("UNCHECKED_CAST")
    private fun <T> any(): T = ArgumentMatchers.any() as T

    @Test
    fun `deve normalizar link ao criar`() {
        val entity = URLMonitorada(link = "https://example.com")
        val response = URLMonitoradaResponseDTO(UUID.randomUUID(), "https://example.com", null, false)
        var capturedDTO: URLMonitoradaCreateDTO? = null

        `when`(urlMonitoradaMapper.toEntity(any())).thenAnswer { invocation ->
            capturedDTO = invocation.getArgument(0)
            entity
        }
        `when`(urlMonitoradaRepository.save(entity)).thenReturn(entity)
        `when`(urlMonitoradaMapper.toResponseDTO(entity)).thenReturn(response)

        service.create(
            URLMonitoradaCreateDTO(
                link = "  HTTPS://EXAMPLE.COM:443/  ",
                ip = null
            )
        )

        assertEquals("https://example.com", capturedDTO?.link)
        verify(urlMonitoradaRepository).save(entity)
    }

    @Test
    fun `deve normalizar link ao buscar`() {
        val entity = URLMonitorada(link = "https://example.com")
        val response = URLMonitoradaResponseDTO(UUID.randomUUID(), "https://example.com", null, false)

        `when`(urlMonitoradaRepository.findByLink("https://example.com"))
            .thenReturn(Optional.of(entity))
        `when`(urlMonitoradaMapper.toResponseDTO(entity)).thenReturn(response)

        val result = service.findByLink("HTTPS://EXAMPLE.COM:443/")

        assertEquals("https://example.com", result.link)
        verify(urlMonitoradaRepository).findByLink("https://example.com")
    }

    @Test
    fun `deve normalizar link ao atualizar`() {
        val id = UUID.randomUUID()
        val entity = URLMonitorada(id = id, link = "https://old.com")
        val response = URLMonitoradaResponseDTO(id, "https://new.com", null, false)
        var capturedDTO: URLMonitoradaUpdateDTO? = null

        `when`(urlMonitoradaRepository.findById(id)).thenReturn(Optional.of(entity))
        `when`(urlMonitoradaRepository.save(entity)).thenReturn(entity)
        `when`(urlMonitoradaMapper.toResponseDTO(entity)).thenReturn(response)
        doAnswer { invocation ->
            capturedDTO = invocation.getArgument(0)
            null
        }.`when`(urlMonitoradaMapper).updateEntityFromDTO(any(), any())

        service.update(
            id,
            URLMonitoradaUpdateDTO(
                link = "HTTPS://NEW.COM:443/",
                ip = null,
                padrao = null
            )
        )

        assertEquals("https://new.com", capturedDTO?.link)
        assertEquals(null, capturedDTO?.ip)
        assertEquals(null, capturedDTO?.padrao)
    }
}
