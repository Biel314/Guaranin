package br.edu.fatec.tcc.guaranin.mapper

import br.edu.fatec.tcc.guaranin.dto.UsuarioURLCreateDTO
import br.edu.fatec.tcc.guaranin.dto.UsuarioURLResponseDTO
import br.edu.fatec.tcc.guaranin.dto.UsuarioURLUpdateDTO
import br.edu.fatec.tcc.guaranin.model.UsuarioURL
import org.mapstruct.*

/**
 * Mapper MapStruct para conversão entre entidade [UsuarioURL] e DTOs.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface UsuarioURLMapper {

    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(source = "urlMonitorada.id", target = "urlMonitoradaId")
    fun toResponseDTO(usuarioURL: UsuarioURL): UsuarioURLResponseDTO

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "urlMonitorada", ignore = true)
    fun toEntity(dto: UsuarioURLCreateDTO): UsuarioURL

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "urlMonitorada", ignore = true)
    fun updateEntityFromDTO(dto: UsuarioURLUpdateDTO, @MappingTarget usuarioURL: UsuarioURL)
}
