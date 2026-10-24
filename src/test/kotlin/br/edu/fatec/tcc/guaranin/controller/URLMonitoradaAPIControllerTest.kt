package br.edu.fatec.tcc.guaranin.controller

import br.edu.fatec.tcc.guaranin.controller.urlmonitorada.URLMonitoradaAPIController
import br.edu.fatec.tcc.guaranin.controller.urlmonitorada.URLMonitoradaAPIExceptionHandler
import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLMonitoradaCreateDTO
import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLMonitoradaResponseDTO
import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLMonitoradaUpdateDTO
import br.edu.fatec.tcc.guaranin.exception.urlmonitorada.URLMonitoradaNotFoundException
import br.edu.fatec.tcc.guaranin.service.urlmonitorada.URLMonitoradaService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import tools.jackson.databind.json.JsonMapper
import java.util.*

/**
 * Testes de unidade do [URLMonitoradaAPIController].
 *
 * Utiliza [MockMvc] em modo standalone, com [URLMonitoradaService] mockado,
 * [JacksonJsonHttpMessageConverter] para serialização compatível com Jackson 3
 * e os handlers [URLMonitoradaAPIExceptionHandler] e [GlobalAPIExceptionHandler]
 * para o contrato de erros.
 */
class URLMonitoradaAPIControllerTest {

    private lateinit var mockMvc: MockMvc

    private lateinit var urlMonitoradaService: URLMonitoradaService

    private val objectMapper = JsonMapper.builder().build()

    /**
     * Atalho tipado para [ArgumentMatchers.any].
     *
     * @param T tipo do argumento esperado.
     * @return valor nulo tipado como [T], usado apenas como matcher do Mockito.
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T> any(): T = ArgumentMatchers.any() as T

    @BeforeEach
    fun setup() {
        urlMonitoradaService = mock(URLMonitoradaService::class.java)
        val controller = URLMonitoradaAPIController(urlMonitoradaService)
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
            .setMessageConverters(JacksonJsonHttpMessageConverter(objectMapper))
            .setControllerAdvice(URLMonitoradaAPIExceptionHandler(), GlobalAPIExceptionHandler())
            .build()
    }

    @Test
    fun `deve listar URLs monitoradas com paginacao`() {
        val responseDTO = URLMonitoradaResponseDTO(
            id = UUID.randomUUID(),
            link = "https://exemplo.com",
            ip = "127.0.0.1",
            padrao = false
        )
        val page = PageImpl(listOf(responseDTO), PageRequest.of(0, 20), 1)

        `when`(urlMonitoradaService.findAll(any())).thenReturn(page)

        mockMvc.perform(get("/api/urlmonitorada").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].link").value("https://exemplo.com"))

        verify(urlMonitoradaService).findAll(PageRequest.of(0, 20))
    }

    @Test
    fun `deve buscar URL monitorada por ID com sucesso`() {
        val id = UUID.randomUUID()
        val responseDTO = URLMonitoradaResponseDTO(id, "https://exemplo.com", null, false)

        `when`(urlMonitoradaService.findById(any())).thenReturn(responseDTO)

        mockMvc.perform(get("/api/urlmonitorada/{id}", id).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.link").value("https://exemplo.com"))

        verify(urlMonitoradaService).findById(id)
    }

    @Test
    fun `deve retornar not found ao buscar URL monitorada por ID inexistente`() {
        val id = UUID.randomUUID()
        `when`(urlMonitoradaService.findById(any())).thenThrow(URLMonitoradaNotFoundException())

        mockMvc.perform(get("/api/urlmonitorada/{id}", id).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.error").value("URL monitorada não encontrada"))

        verify(urlMonitoradaService).findById(id)
    }

    @Test
    fun `deve retornar bad request ao buscar URL monitorada com ID invalido`() {
        mockMvc.perform(get("/api/urlmonitorada/{id}", "id-invalido").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("Parâmetro inválido"))

        verify(urlMonitoradaService, never()).findById(any())
    }

    @Test
    fun `deve buscar URL monitorada por link com sucesso`() {
        val responseDTO = URLMonitoradaResponseDTO(
            id = UUID.randomUUID(),
            link = "https://exemplo.com",
            ip = null,
            padrao = false
        )

        `when`(urlMonitoradaService.findByLink(any())).thenReturn(responseDTO)

        mockMvc.perform(
            get("/api/urlmonitorada/link")
                .param("link", "https://exemplo.com")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.link").value("https://exemplo.com"))

        verify(urlMonitoradaService).findByLink("https://exemplo.com")
    }

    @Test
    fun `deve retornar bad request ao buscar URL monitorada com link invalido`() {
        mockMvc.perform(
            get("/api/urlmonitorada/link")
                .param("link", "nao-e-uma-url")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").isString)

        verify(urlMonitoradaService, never()).findByLink(any())
    }

    @Test
    fun `deve retornar bad request quando link estiver em branco`() {
        mockMvc.perform(
            get("/api/urlmonitorada/link")
                .param("link", "")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").isString)

        verify(urlMonitoradaService, never()).findByLink(any())
    }

    @Test
    fun `deve retornar bad request quando link estiver ausente`() {
        mockMvc.perform(get("/api/urlmonitorada/link").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("Parâmetro obrigatório ausente: link"))

        verify(urlMonitoradaService, never()).findByLink(any())
    }

    @Test
    fun `deve criar URL monitorada com sucesso`() {
        val createDTO = URLMonitoradaCreateDTO(link = "https://exemplo.com", ip = null)
        val responseDTO = URLMonitoradaResponseDTO(UUID.randomUUID(), "https://exemplo.com", null, false)

        `when`(urlMonitoradaService.create(any())).thenReturn(responseDTO)

        mockMvc.perform(
            post("/api/urlmonitorada")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.link").value("https://exemplo.com"))

        verify(urlMonitoradaService).create(createDTO)
    }

    @Test
    fun `deve retornar bad request ao criar URL monitorada com link invalido`() {
        val createDTO = URLMonitoradaCreateDTO(link = "", ip = null)

        mockMvc.perform(
            post("/api/urlmonitorada")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").isString)

        verify(urlMonitoradaService, never()).create(any())
    }

    @Test
    fun `deve retornar conflict ao criar URL monitorada duplicada`() {
        val createDTO = URLMonitoradaCreateDTO(link = "https://exemplo.com", ip = null)

        `when`(urlMonitoradaService.create(any()))
            .thenThrow(DataIntegrityViolationException("link duplicado"))

        mockMvc.perform(
            post("/api/urlmonitorada")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO))
        )
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.error").value("Conflito de dados: registro já existe"))
    }

    @Test
    fun `deve atualizar URL monitorada com sucesso`() {
        val id = UUID.randomUUID()
        val updateDTO = URLMonitoradaUpdateDTO(link = "https://novo.com", ip = null, padrao = null)
        val responseDTO = URLMonitoradaResponseDTO(id, "https://novo.com", null, false)

        `when`(urlMonitoradaService.update(any(), any())).thenReturn(responseDTO)

        mockMvc.perform(
            put("/api/urlmonitorada/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.link").value("https://novo.com"))

        verify(urlMonitoradaService).update(id, updateDTO)
    }

    @Test
    fun `deve retornar not found ao atualizar URL monitorada inexistente`() {
        val id = UUID.randomUUID()
        val updateDTO = URLMonitoradaUpdateDTO(link = "https://novo.com", ip = null, padrao = null)

        `when`(urlMonitoradaService.update(any(), any())).thenThrow(URLMonitoradaNotFoundException())

        mockMvc.perform(
            put("/api/urlmonitorada/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO))
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.error").value("URL monitorada não encontrada"))
    }

    @Test
    fun `deve deletar URL monitorada com sucesso`() {
        val id = UUID.randomUUID()
        `when`(urlMonitoradaService.delete(any()))
            .thenReturn(URLMonitoradaResponseDTO(id, "https://exemplo.com", null, false))

        mockMvc.perform(delete("/api/urlmonitorada/{id}", id))
            .andExpect(status().isNoContent)
            .andExpect(content().string(""))

        verify(urlMonitoradaService).delete(id)
    }

    @Test
    fun `deve retornar not found ao deletar URL monitorada inexistente`() {
        val id = UUID.randomUUID()
        `when`(urlMonitoradaService.delete(any())).thenThrow(URLMonitoradaNotFoundException())

        mockMvc.perform(delete("/api/urlmonitorada/{id}", id))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.error").value("URL monitorada não encontrada"))
    }
}
