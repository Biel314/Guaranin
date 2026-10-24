package br.edu.fatec.tcc.guaranin.service.usuario

import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioURLResponseDTO
import br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioNotFoundException
import br.edu.fatec.tcc.guaranin.mapper.usuario.UsuarioURLMapper
import br.edu.fatec.tcc.guaranin.model.UsuarioURL
import br.edu.fatec.tcc.guaranin.repository.urlmonitorada.URLMonitoradaRepository
import br.edu.fatec.tcc.guaranin.repository.usuario.UsuarioRepository
import br.edu.fatec.tcc.guaranin.repository.usuario.UsuarioURLRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.UUID

/**
 * Testes unitários de [UsuarioURLService], com repositórios e mapper mockados.
 */
class UsuarioURLServiceTest {

    private val usuarioURLRepository = mock(UsuarioURLRepository::class.java)
    private val usuarioRepository = mock(UsuarioRepository::class.java)
    private val urlMonitoradaRepository = mock(URLMonitoradaRepository::class.java)
    private val usuarioURLMapper = mock(UsuarioURLMapper::class.java)
    private val service = UsuarioURLService(
        usuarioURLRepository,
        usuarioURLMapper,
        usuarioRepository,
        urlMonitoradaRepository
    )

    private val pageable = PageRequest.of(0, 20)

    /**
     * Verifica se uma página com conteúdo é mapeada sem consultar a existência
     * do usuário.
     */
    @Test
    fun `deve listar associacoes sem verificar usuario quando ha conteudo`() {
        val usuarioId = UUID.randomUUID()
        val usuarioURL = UsuarioURL()
        val responseDTO = UsuarioURLResponseDTO(
            id = UUID.randomUUID(),
            usuarioId = usuarioId,
            urlMonitoradaId = UUID.randomUUID(),
            qtdAcesso = 1,
            tempoAtivo = 10
        )
        val page = PageImpl(listOf(usuarioURL), pageable, 1)

        `when`(usuarioURLRepository.findByUsuarioId(usuarioId, pageable)).thenReturn(page)
        `when`(usuarioURLMapper.toResponseDTO(usuarioURL)).thenReturn(responseDTO)

        val result = service.findByUsuarioId(usuarioId, pageable)

        assertEquals(listOf(responseDTO), result.content)
        verifyNoInteractions(usuarioRepository)
    }

    /**
     * Verifica se uma página vazia de um usuário existente retorna página vazia
     * sem lançar exceção.
     */
    @Test
    fun `deve retornar pagina vazia quando usuario existe sem associacoes`() {
        val usuarioId = UUID.randomUUID()
        val page = PageImpl<UsuarioURL>(emptyList(), pageable, 0)

        `when`(usuarioURLRepository.findByUsuarioId(usuarioId, pageable)).thenReturn(page)
        `when`(usuarioRepository.existsById(usuarioId)).thenReturn(true)

        val result = service.findByUsuarioId(usuarioId, pageable)

        assertTrue(result.isEmpty)
        verify(usuarioRepository).existsById(usuarioId)
        verifyNoInteractions(usuarioURLMapper)
    }

    /**
     * Verifica se uma página vazia de um usuário inexistente lança
     * [UsuarioNotFoundException].
     */
    @Test
    fun `deve lancar excecao quando usuario nao existe e pagina esta vazia`() {
        val usuarioId = UUID.randomUUID()
        val page = PageImpl<UsuarioURL>(emptyList(), pageable, 0)

        `when`(usuarioURLRepository.findByUsuarioId(usuarioId, pageable)).thenReturn(page)
        `when`(usuarioRepository.existsById(usuarioId)).thenReturn(false)

        assertThrows(UsuarioNotFoundException::class.java) {
            service.findByUsuarioId(usuarioId, pageable)
        }

        verify(usuarioRepository).existsById(usuarioId)
        verifyNoInteractions(usuarioURLMapper)
    }

    /**
     * Verifica se uma página com conteúdo é mapeada sem consultar a existência
     * do usuário ao filtrar por tempo ativo mínimo.
     */
    @Test
    fun `deve listar associacoes por tempo ativo sem verificar usuario quando ha conteudo`() {
        val usuarioId = UUID.randomUUID()
        val tempoAtivo = 10
        val usuarioURL = UsuarioURL()
        val responseDTO = UsuarioURLResponseDTO(
            id = UUID.randomUUID(),
            usuarioId = usuarioId,
            urlMonitoradaId = UUID.randomUUID(),
            qtdAcesso = 1,
            tempoAtivo = 10
        )
        val page = PageImpl(listOf(usuarioURL), pageable, 1)

        `when`(
            usuarioURLRepository.findByUsuarioIdAndTempoAtivoGreaterThanEqual(
                usuarioId,
                tempoAtivo,
                pageable
            )
        ).thenReturn(page)
        `when`(usuarioURLMapper.toResponseDTO(usuarioURL)).thenReturn(responseDTO)

        val result = service.findByUsuarioIdAndTempoAtivoGreaterThanEqual(
            usuarioId,
            tempoAtivo,
            pageable
        )

        assertEquals(listOf(responseDTO), result.content)
        verify(usuarioURLRepository).findByUsuarioIdAndTempoAtivoGreaterThanEqual(
            usuarioId,
            tempoAtivo,
            pageable
        )
        verifyNoInteractions(usuarioRepository)
    }

    /**
     * Verifica se uma página vazia de um usuário existente retorna página vazia
     * ao filtrar por tempo ativo mínimo.
     */
    @Test
    fun `deve retornar pagina vazia por tempo ativo quando usuario existe`() {
        val usuarioId = UUID.randomUUID()
        val tempoAtivo = 10
        val page = PageImpl<UsuarioURL>(emptyList(), pageable, 0)

        `when`(
            usuarioURLRepository.findByUsuarioIdAndTempoAtivoGreaterThanEqual(
                usuarioId,
                tempoAtivo,
                pageable
            )
        ).thenReturn(page)
        `when`(usuarioRepository.existsById(usuarioId)).thenReturn(true)

        val result = service.findByUsuarioIdAndTempoAtivoGreaterThanEqual(
            usuarioId,
            tempoAtivo,
            pageable
        )

        assertTrue(result.isEmpty)
        verify(usuarioRepository).existsById(usuarioId)
        verifyNoInteractions(usuarioURLMapper)
    }

    /**
     * Verifica se uma página vazia de um usuário inexistente lança
     * [UsuarioNotFoundException] ao filtrar por tempo ativo mínimo.
     */
    @Test
    fun `deve lancar excecao por tempo ativo quando usuario nao existe`() {
        val usuarioId = UUID.randomUUID()
        val tempoAtivo = 10
        val page = PageImpl<UsuarioURL>(emptyList(), pageable, 0)

        `when`(
            usuarioURLRepository.findByUsuarioIdAndTempoAtivoGreaterThanEqual(
                usuarioId,
                tempoAtivo,
                pageable
            )
        ).thenReturn(page)
        `when`(usuarioRepository.existsById(usuarioId)).thenReturn(false)

        assertThrows(UsuarioNotFoundException::class.java) {
            service.findByUsuarioIdAndTempoAtivoGreaterThanEqual(
                usuarioId,
                tempoAtivo,
                pageable
            )
        }

        verify(usuarioRepository).existsById(usuarioId)
        verifyNoInteractions(usuarioURLMapper)
    }
}
