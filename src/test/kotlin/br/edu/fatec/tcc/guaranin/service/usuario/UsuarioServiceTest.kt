package br.edu.fatec.tcc.guaranin.service.usuario

import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioCreateDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioResponseDTO
import br.edu.fatec.tcc.guaranin.mapper.usuario.UsuarioMapper
import br.edu.fatec.tcc.guaranin.model.Usuario
import br.edu.fatec.tcc.guaranin.repository.usuario.UsuarioRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.UUID

/**
 * Testes unitários de [UsuarioService], com repositório e mapper mockados.
 */
class UsuarioServiceTest {

    private val usuarioRepository = mock(UsuarioRepository::class.java)
    private val usuarioMapper = mock(UsuarioMapper::class.java)
    private val service = UsuarioService(usuarioRepository, usuarioMapper)

    /**
     * Verifica se a criação persiste a entidade uma única vez e retorna o DTO
     * mapeado da entidade salva.
     */
    @Test
    fun `deve salvar usuario apenas uma vez ao criar`() {
        val createDTO = UsuarioCreateDTO(
            apelido = "testuser",
            email = "test@fatec.br",
            password = "Password@123"
        )
        val entity = Usuario(
            apelido = "testuser",
            email = "test@fatec.br",
            password = "Password@123"
        )
        val savedEntity = Usuario(
            id = UUID.randomUUID(),
            apelido = "testuser",
            email = "test@fatec.br",
            password = "Password@123"
        )
        val responseDTO = UsuarioResponseDTO(
            id = savedEntity.id!!,
            apelido = "testuser",
            email = "test@fatec.br"
        )

        `when`(usuarioMapper.toEntity(createDTO)).thenReturn(entity)
        `when`(usuarioRepository.save(entity)).thenReturn(savedEntity)
        `when`(usuarioMapper.toResponseDTO(savedEntity)).thenReturn(responseDTO)

        val result = service.create(createDTO)

        assertEquals(responseDTO, result)
        verify(usuarioRepository, times(1)).save(entity)
        verify(usuarioMapper, times(1)).toResponseDTO(savedEntity)
    }
}
