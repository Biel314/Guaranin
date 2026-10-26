package br.edu.fatec.tcc.guaranin.controller

import br.edu.fatec.tcc.guaranin.controller.urlmonitorada.URLHistoricoAPIController
import br.edu.fatec.tcc.guaranin.controller.urlmonitorada.URLHistoricoAPIExceptionHandler
import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLHistoricoCreateDTO
import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLHistoricoResponseDTO
import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLHistoricoUpdateDTO
import br.edu.fatec.tcc.guaranin.exception.urlmonitorada.URLHistoricoNotFoundException
import br.edu.fatec.tcc.guaranin.service.urlmonitorada.URLHistoricoService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
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
import tools.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import java.util.*

/**
 * Testes de unidade do [URLHistoricoAPIController].
 *
 * Utiliza [MockMvc] em modo standalone, com [URLHistoricoService] mockado,
 * [JacksonJsonHttpMessageConverter] para serialização e os handlers
 * [URLHistoricoAPIExceptionHandler] e [GlobalAPIExceptionHandler] para o
 * contrato de erros. Cobre listagem, consultas, criação, atualização e remoção.
 */
class URLHistoricoAPIControllerTest {

    private lateinit var mockMvc: MockMvc

    private lateinit var urlHistoricoService: URLHistoricoService

    private val objectMapper = JsonMapper.builder()
        .addModule(KotlinModule.Builder().build())
        .build()

    private val dataHoraAcesso = LocalDateTime.of(2020, 1, 1, 12, 0)

    /**
     * Atalho tipado para [ArgumentMatchers.any].
     *
     * @param T tipo do argumento esperado.
     * @return valor nulo tipado como [T], usado apenas como matcher do Mockito.
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T> any(): T = ArgumentMatchers.any() as T

    /**
     * Cria um DTO de resposta usado como fixture dos testes.
     *
     * @param id identificador do histórico.
     * @param usuarioUrlId identificador da associação `UsuarioURL`.
     * @return DTO preenchido com a data/hora fixa dos testes.
     */
    private fun response(
        id: UUID = UUID.randomUUID(),
        usuarioUrlId: UUID = UUID.randomUUID()
    ) = URLHistoricoResponseDTO(id, usuarioUrlId, dataHoraAcesso)

    /**
     * Inicializa o ambiente antes de cada teste.
     *
     * Cria o mock de [URLHistoricoService], instancia o controller e configura
     * o [MockMvc] com o resolver de `Pageable`, o conversor JSON e os handlers
     * de exceção da API.
     */
    @BeforeEach
    fun setup() {
        urlHistoricoService = mock(URLHistoricoService::class.java)
        val controller = URLHistoricoAPIController(urlHistoricoService)
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
            .setMessageConverters(JacksonJsonHttpMessageConverter(objectMapper))
            .setControllerAdvice(URLHistoricoAPIExceptionHandler(), GlobalAPIExceptionHandler())
            .build()
    }

    /**
     * Verifica se a listagem paginada retorna `200 OK` com o conteúdo esperado
     * e se o `Pageable` padrão é repassado ao serviço.
     */
    @Test
    fun `deve listar historicos com paginacao`() {
        val responseDTO = response()
        val page = PageImpl(listOf(responseDTO), PageRequest.of(0, 20), 1)

        `when`(urlHistoricoService.findAll(any())).thenReturn(page)

        mockMvc.perform(get("/api/urlhistorico").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].id").value(responseDTO.id.toString()))
            .andExpect(jsonPath("$.content[0].usuarioUrlId").value(responseDTO.usuarioUrlId.toString()))

        verify(urlHistoricoService).findAll(PageRequest.of(0, 20))
    }

    /**
     * Verifica se a busca por ID existente retorna `200 OK` e os dados do
     * histórico.
     */
    @Test
    fun `deve buscar historico por ID com sucesso`() {
        val id = UUID.randomUUID()
        val responseDTO = response(id)

        `when`(urlHistoricoService.findById(any())).thenReturn(responseDTO)

        mockMvc.perform(get("/api/urlhistorico/{id}", id).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.usuarioUrlId").value(responseDTO.usuarioUrlId.toString()))

        verify(urlHistoricoService).findById(id)
    }

    /**
     * Verifica se a busca por ID inexistente retorna `404 Not Found` com a
     * mensagem de erro esperada.
     */
    @Test
    fun `deve retornar not found ao buscar historico por ID inexistente`() {
        val id = UUID.randomUUID()
        `when`(urlHistoricoService.findById(any())).thenThrow(URLHistoricoNotFoundException())

        mockMvc.perform(get("/api/urlhistorico/{id}", id).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.error").value("URL histórico não encontrado"))

        verify(urlHistoricoService).findById(id)
    }

    /**
     * Verifica se um ID de rota inválido retorna `400 Bad Request` sem acionar
     * o serviço.
     */
    @Test
    fun `deve retornar bad request ao buscar historico com ID invalido`() {
        mockMvc.perform(get("/api/urlhistorico/{id}", "id-invalido").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("Parâmetro inválido"))

        verify(urlHistoricoService, never()).findById(any())
    }

    /**
     * Verifica se a busca paginada por usuário retorna `200 OK` e repassa o
     * identificador ao serviço.
     */
    @Test
    fun `deve buscar historicos por usuario com paginacao`() {
        val usuarioId = UUID.randomUUID()
        val page = PageImpl(listOf(response()), PageRequest.of(0, 20), 1)

        `when`(urlHistoricoService.findAllByUsuarioId(any(), any())).thenReturn(page)

        mockMvc.perform(
            get("/api/urlhistorico/usuario/{id}", usuarioId)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)

        verify(urlHistoricoService).findAllByUsuarioId(usuarioId, PageRequest.of(0, 20))
    }

    /**
     * Verifica se um ID de usuário inválido retorna `400 Bad Request` sem
     * acionar o serviço.
     */
    @Test
    fun `deve retornar bad request ao buscar historicos com ID de usuario invalido`() {
        mockMvc.perform(
            get("/api/urlhistorico/usuario/{id}", "id-invalido")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("Parâmetro inválido"))

        verify(urlHistoricoService, never()).findAllByUsuarioId(any(), any())
    }

    /**
     * Verifica se a busca por data e hora de acesso retorna `200 OK` e
     * converte o caminho ISO-8601 corretamente.
     */
    @Test
    fun `deve buscar historicos por data e hora de acesso`() {
        val page = PageImpl(listOf(response()), PageRequest.of(0, 20), 1)

        `when`(urlHistoricoService.findAllByDataHoraAcesso(any(), any())).thenReturn(page)

        mockMvc.perform(
            get("/api/urlhistorico/acesso/{dataHoraAcesso}", dataHoraAcesso.toString())
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)

        verify(urlHistoricoService)
            .findAllByDataHoraAcesso(dataHoraAcesso, PageRequest.of(0, 20))
    }

    /**
     * Verifica se uma data e hora inválida no caminho retorna `400 Bad Request`
     * sem acionar o serviço.
     */
    @Test
    fun `deve retornar bad request ao buscar historicos com data e hora invalida`() {
        mockMvc.perform(
            get("/api/urlhistorico/acesso/{dataHoraAcesso}", "data-invalida")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("Parâmetro inválido"))

        verify(urlHistoricoService, never()).findAllByDataHoraAcesso(any(), any())
    }

    /**
     * Verifica se a busca por associação `UsuarioURL` retorna `200 OK`.
     */
    @Test
    fun `deve buscar historico por Usuario URL com sucesso`() {
        val usuarioURLId = UUID.randomUUID()
        val responseDTO = response(usuarioUrlId = usuarioURLId)

        `when`(urlHistoricoService.findByUsuarioURL(any())).thenReturn(responseDTO)

        mockMvc.perform(
            get("/api/urlhistorico/usuario_url/{usuarioURLId}", usuarioURLId)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.usuarioUrlId").value(usuarioURLId.toString()))

        verify(urlHistoricoService).findByUsuarioURL(usuarioURLId)
    }

    /**
     * Verifica se a busca por associação `UsuarioURL` inexistente retorna
     * `404 Not Found`.
     */
    @Test
    fun `deve retornar not found ao buscar historico por Usuario URL inexistente`() {
        val usuarioURLId = UUID.randomUUID()

        `when`(urlHistoricoService.findByUsuarioURL(any()))
            .thenThrow(URLHistoricoNotFoundException())

        mockMvc.perform(
            get("/api/urlhistorico/usuario_url/{usuarioURLId}", usuarioURLId)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.error").value("URL histórico não encontrado"))

        verify(urlHistoricoService).findByUsuarioURL(usuarioURLId)
    }

    /**
     * Verifica se um ID de associação `UsuarioURL` inválido retorna
     * `400 Bad Request` sem acionar o serviço.
     */
    @Test
    fun `deve retornar bad request ao buscar historico com Usuario URL invalido`() {
        mockMvc.perform(
            get("/api/urlhistorico/usuario_url/{usuarioURLId}", "id-invalido")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("Parâmetro inválido"))

        verify(urlHistoricoService, never()).findByUsuarioURL(any())
    }

    /**
     * Verifica se a criação com JSON válido retorna `201 Created` e repassa o
     * DTO ao serviço.
     */
    @Test
    fun `deve criar historico com sucesso`() {
        val createDTO = URLHistoricoCreateDTO(
            usuarioUrlId = UUID.randomUUID(),
            dataHoraAcesso = dataHoraAcesso
        )

        `when`(urlHistoricoService.create(any())).thenReturn(response())

        mockMvc.perform(
            post("/api/urlhistorico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.usuarioUrlId").exists())

        verify(urlHistoricoService).create(createDTO)
    }

    /**
     * Verifica se a criação sem `usuarioUrlId` retorna `400 Bad Request` sem
     * acionar o serviço.
     */
    @Test
    fun `deve retornar bad request ao criar historico sem Usuario URL`() {
        mockMvc.perform(
            post("/api/urlhistorico")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"dataHoraAcesso":"2020-01-01T12:00:00"}""")
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").isString)

        verify(urlHistoricoService, never()).create(any())
    }

    /**
     * Verifica se a criação com data/hora futura retorna `400 Bad Request` sem
     * acionar o serviço.
     */
    @Test
    fun `deve retornar bad request ao criar historico com data futura`() {
        val createDTO = URLHistoricoCreateDTO(
            usuarioUrlId = UUID.randomUUID(),
            dataHoraAcesso = LocalDateTime.now().plusDays(1).withNano(0)
        )

        mockMvc.perform(
            post("/api/urlhistorico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").isString)

        verify(urlHistoricoService, never()).create(any())
    }

    /**
     * Verifica se um corpo JSON malformado retorna `400 Bad Request` com a
     * mensagem padronizada.
     */
    @Test
    fun `deve retornar bad request ao criar historico com corpo invalido`() {
        mockMvc.perform(
            post("/api/urlhistorico")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{")
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("Corpo da requisição inválido"))

        verify(urlHistoricoService, never()).create(any())
    }

    /**
     * Verifica se a atualização com dados válidos retorna `200 OK` e repassa o
     * DTO ao serviço.
     */
    @Test
    fun `deve atualizar historico com sucesso`() {
        val id = UUID.randomUUID()
        val updateDTO = URLHistoricoUpdateDTO(
            usuarioUrlId = UUID.randomUUID(),
            dataHoraAcesso = dataHoraAcesso
        )

        `when`(urlHistoricoService.update(any(), any())).thenReturn(response(id))

        mockMvc.perform(
            put("/api/urlhistorico/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(id.toString()))

        verify(urlHistoricoService).update(id, updateDTO)
    }

    /**
     * Verifica se a atualização de um histórico inexistente retorna
     * `404 Not Found`.
     */
    @Test
    fun `deve retornar not found ao atualizar historico inexistente`() {
        val id = UUID.randomUUID()
        val updateDTO = URLHistoricoUpdateDTO(
            usuarioUrlId = null,
            dataHoraAcesso = null
        )

        `when`(urlHistoricoService.update(any(), any()))
            .thenThrow(URLHistoricoNotFoundException())

        mockMvc.perform(
            put("/api/urlhistorico/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO))
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.error").value("URL histórico não encontrado"))
    }

    /**
     * Verifica se a atualização com data/hora futura retorna `400 Bad Request`
     * sem acionar o serviço.
     */
    @Test
    fun `deve retornar bad request ao atualizar historico com data futura`() {
        val id = UUID.randomUUID()
        val updateDTO = URLHistoricoUpdateDTO(
            usuarioUrlId = null,
            dataHoraAcesso = LocalDateTime.now().plusDays(1).withNano(0)
        )

        mockMvc.perform(
            put("/api/urlhistorico/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").isString)

        verify(urlHistoricoService, never()).update(any(), any())
    }

    /**
     * Verifica se um ID de rota inválido na atualização retorna
     * `400 Bad Request` sem acionar o serviço.
     */
    @Test
    fun `deve retornar bad request ao atualizar historico com ID invalido`() {
        val updateDTO = URLHistoricoUpdateDTO(
            usuarioUrlId = null,
            dataHoraAcesso = null
        )

        mockMvc.perform(
            put("/api/urlhistorico/{id}", "id-invalido")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("Parâmetro inválido"))

        verify(urlHistoricoService, never()).update(any(), any())
    }

    /**
     * Verifica se a remoção de um histórico existente retorna
     * `204 No Content` sem corpo e repassa o ID ao serviço.
     */
    @Test
    fun `deve deletar historico com sucesso`() {
        val id = UUID.randomUUID()
        `when`(urlHistoricoService.delete(any())).thenReturn(response(id))

        mockMvc.perform(delete("/api/urlhistorico/{id}", id))
            .andExpect(status().isNoContent)
            .andExpect(content().string(""))

        verify(urlHistoricoService).delete(id)
    }

    /**
     * Verifica se a remoção de um histórico inexistente retorna
     * `404 Not Found`.
     */
    @Test
    fun `deve retornar not found ao deletar historico inexistente`() {
        val id = UUID.randomUUID()
        `when`(urlHistoricoService.delete(any()))
            .thenThrow(URLHistoricoNotFoundException())

        mockMvc.perform(delete("/api/urlhistorico/{id}", id))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.error").value("URL histórico não encontrado"))

        verify(urlHistoricoService).delete(id)
    }

    /**
     * Verifica se um ID inválido na remoção retorna `400 Bad Request` sem
     * acionar o serviço.
     */
    @Test
    fun `deve retornar bad request ao deletar historico com ID invalido`() {
        mockMvc.perform(delete("/api/urlhistorico/{id}", "id-invalido"))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("Parâmetro inválido"))

        verify(urlHistoricoService, never()).delete(any())
    }
}
