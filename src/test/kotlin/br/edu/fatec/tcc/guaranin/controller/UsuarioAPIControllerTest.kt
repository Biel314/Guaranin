package br.edu.fatec.tcc.guaranin.controller

import br.edu.fatec.tcc.guaranin.controller.GlobalAPIExceptionHandler
import br.edu.fatec.tcc.guaranin.controller.usuario.UsuarioAPIController
import br.edu.fatec.tcc.guaranin.controller.usuario.UsuarioAPIExceptionHandler
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioCreateDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioResponseDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioUpdateDTO
import br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioNotFoundException
import br.edu.fatec.tcc.guaranin.service.usuario.UsuarioService
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
import java.util.*

/**
 * Testes de unidade do [br.edu.fatec.tcc.guaranin.controller.usuario.UsuarioAPIController].
 *
 * Utiliza [MockMvc] em modo standalone, com [UsuarioService] mockado,
 * [JacksonJsonHttpMessageConverter] para serialização compatível com Jackson 3
 * e os handlers [br.edu.fatec.tcc.guaranin.controller.usuario.UsuarioAPIExceptionHandler]
 * e [br.edu.fatec.tcc.guaranin.controller.GlobalAPIExceptionHandler] para o contrato de erros.
 * Cobre criação, consulta, listagem paginada, atualização completa, atualização
 * de apelido e remoção de usuários.
 */
class UsuarioAPIControllerTest {

    /** Executor de requisições HTTP simuladas usado pelos testes. */
    private lateinit var mockMvc: MockMvc

    /** Mock de [UsuarioService] que substitui a camada de serviço real. */
    private lateinit var usuarioService: UsuarioService

    /** Mapper Jackson 3 usado para serializar os DTOs enviados nas requisições. */
    private val objectMapper = JsonMapper.builder().build()

    /**
     * Atalho tipado para [ArgumentMatchers.any].
     *
     * @param T tipo do argumento esperado.
     * @return valor nulo tipado como [T], usado apenas como matcher do Mockito.
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T> any(): T = ArgumentMatchers.any() as T

    /**
     * Inicializa o ambiente antes de cada teste.
     *
     * Cria o mock de [UsuarioService], instancia o [br.edu.fatec.tcc.guaranin.controller.usuario.UsuarioAPIController] e configura
     * o [MockMvc] com o resolver de `Pageable`, o conversor JSON do Jackson 3 e
     * os handlers [br.edu.fatec.tcc.guaranin.controller.usuario.UsuarioAPIExceptionHandler]
     * e [br.edu.fatec.tcc.guaranin.controller.GlobalAPIExceptionHandler].
     */
    @BeforeEach
    fun setup() {
        usuarioService = mock(UsuarioService::class.java)
        val controller = UsuarioAPIController(usuarioService)
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
            .setMessageConverters(JacksonJsonHttpMessageConverter(objectMapper))
            .setControllerAdvice(UsuarioAPIExceptionHandler(), GlobalAPIExceptionHandler())
            .build()
    }

    /**
     * Verifica se a criação de um usuário com JSON válido retorna `201 Created`,
     * se o corpo contém os dados esperados e se o DTO é repassado ao serviço.
     */
    @Test
    fun `deve criar usuario com sucesso com JSON valido`() {
        val createDTO = UsuarioCreateDTO(
            apelido = "testuser",
            email = "test@fatec.br",
            password = "Password@123"
        )
        val responseDTO = UsuarioResponseDTO(
            id = UUID.randomUUID(),
            apelido = "testuser",
            email = "test@fatec.br"
        )

        `when`(usuarioService.create(any())).thenReturn(responseDTO)

        mockMvc.perform(
            post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.apelido").value("testuser"))
            .andExpect(jsonPath("$.email").value("test@fatec.br"))

        verify(usuarioService).create(createDTO)
    }

    /**
     * Verifica se a criação com apelido, e-mail e senha inválidos retorna `400 Bad Request`
     * e se o serviço não é acionado.
     */
    @Test
    fun `deve retornar bad request ao criar usuario com dados invalidos`() {
        val createDTO = UsuarioCreateDTO(
            apelido = "",
            email = "invalid-email",
            password = "123"
        )

        mockMvc.perform(
            post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").isString)

        verify(usuarioService, never()).create(any())
    }

    /**
     * Verifica se a busca por um ID existente retorna `200 OK`,
     * os dados do usuário e se o ID é repassado ao serviço.
     */
    @Test
    fun `deve buscar usuario por ID com sucesso`() {
        val id = UUID.randomUUID()
        val responseDTO = UsuarioResponseDTO(
            id = id,
            apelido = "testuser",
            email = "test@fatec.br"
        )

        `when`(usuarioService.findById(any())).thenReturn(responseDTO)

        mockMvc.perform(
            get("/api/usuarios/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.apelido").value("testuser"))
            .andExpect(jsonPath("$.email").value("test@fatec.br"))

        verify(usuarioService).findById(id)
    }

    /**
     * Verifica se a busca por um ID inexistente retorna `404 Not Found`
     * com a mensagem de erro esperada.
     */
    @Test
    fun `deve retornar not found ao buscar usuario por ID inexistente`() {
        val id = UUID.randomUUID()
        `when`(usuarioService.findById(any())).thenThrow(UsuarioNotFoundException())

        mockMvc.perform(
            get("/api/usuarios/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.error").value("Usuário não encontrado"))

        verify(usuarioService).findById(id)
    }

    /**
     * Verifica se um ID de rota inválido retorna `400 Bad Request`
     * com a mensagem de parâmetro inválido e sem acionar o serviço.
     */
    @Test
    fun `deve retornar bad request ao buscar usuario com ID invalido`() {
        mockMvc.perform(
            get("/api/usuarios/{id}", "id-invalido")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("Parâmetro inválido"))

        verify(usuarioService, never()).findById(any())
    }

    /**
     * Verifica se a listagem paginada retorna `200 OK`, o conteúdo esperado
     * e se o `Pageable` padrão (página 0, tamanho 20) é repassado ao serviço.
     */
    @Test
    fun `deve listar todos os usuarios com paginacao`() {
        val responseDTO = UsuarioResponseDTO(
            id = UUID.randomUUID(),
            apelido = "testuser",
            email = "test@fatec.br"
        )
        val page = PageImpl(listOf(responseDTO), PageRequest.of(0, 20), 1)

        `when`(usuarioService.findAll(any())).thenReturn(page)

        mockMvc.perform(
            get("/api/usuarios")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content[0].apelido").value("testuser"))

        verify(usuarioService).findAll(PageRequest.of(0, 20))
    }

    /**
     * Verifica se a atualização de um usuário com dados válidos retorna `200 OK`,
     * se o corpo contém os dados atualizados e se o DTO é repassado ao serviço.
     */
    @Test
    fun `deve atualizar usuario com sucesso`() {
        val id = UUID.randomUUID()
        val updateDTO = UsuarioUpdateDTO(
            apelido = "updateduser",
            email = "updated@fatec.br",
            password = "NewPassword@123"
        )
        val responseDTO = UsuarioResponseDTO(
            id = id,
            apelido = "updateduser",
            email = "updated@fatec.br"
        )

        `when`(usuarioService.update(any(), any())).thenReturn(responseDTO)

        mockMvc.perform(
            put("/api/usuarios/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.apelido").value("updateduser"))
            .andExpect(jsonPath("$.email").value("updated@fatec.br"))

        verify(usuarioService).update(id, updateDTO)
    }

    /**
     * Verifica se a remoção de um usuário existente retorna `204 No Content`,
     * sem corpo, e se o ID é repassado ao serviço.
     */
    @Test
    fun `deve deletar usuario com sucesso`() {
        val id = UUID.randomUUID()
        val responseDTO = UsuarioResponseDTO(
            id = id,
            apelido = "testuser",
            email = "test@fatec.br"
        )

        `when`(usuarioService.delete(any())).thenReturn(responseDTO)

        mockMvc.perform(
            delete("/api/usuarios/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNoContent)
            .andExpect(content().string(""))

        verify(usuarioService).delete(id)
    }
}
