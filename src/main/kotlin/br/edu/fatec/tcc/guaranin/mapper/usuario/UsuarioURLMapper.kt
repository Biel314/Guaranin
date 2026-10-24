package br.edu.fatec.tcc.guaranin.mapper.usuario

import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioURLCreateDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioURLResponseDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioURLUpdateDTO
import br.edu.fatec.tcc.guaranin.model.UsuarioURL
import org.mapstruct.*

/**
 * Mapper MapStruct para conversão entre a entidade [UsuarioURL] e seus DTOs.
 *
 * A implementação é gerada em tempo de compilação pelo MapStruct via kapt e
 * registrada como bean Spring por `componentModel = "spring"`. As associações
 * `usuario` e `urlMonitorada` não são mapeadas diretamente: o service resolve
 * e atribui as entidades correspondentes antes de persistir.
 *
 * @see UsuarioURL
 * @see UsuarioURLCreateDTO
 * @see UsuarioURLUpdateDTO
 * @see UsuarioURLResponseDTO
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface UsuarioURLMapper {

    /**
     * Converte uma entidade [UsuarioURL] em [UsuarioURLResponseDTO].
     *
     * Os identificadores das associações são extraídos de `usuario.id` e
     * `urlMonitorada.id`.
     *
     * @param usuarioURL entidade de origem.
     * @return DTO de saída pronto para serialização.
     */
    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(source = "urlMonitorada.id", target = "urlMonitoradaId")
    fun toResponseDTO(usuarioURL: UsuarioURL): UsuarioURLResponseDTO

    /**
     * Converte um [UsuarioURLCreateDTO] em uma nova entidade [UsuarioURL].
     *
     * O `id` e as associações são ignorados; o service atribui `usuario` e
     * `urlMonitorada` após resolver os identificadores informados.
     *
     * @param dto dados de entrada para criação.
     * @return nova entidade ainda não persistida.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "urlMonitorada", ignore = true)
    fun toEntity(dto: UsuarioURLCreateDTO): UsuarioURL

    /**
     * Atualiza in-place uma entidade [UsuarioURL] com os dados de
     * [UsuarioURLUpdateDTO].
     *
     * O `id`, as associações e campos nulos preservam os valores atuais.
     *
     * @param dto dados de atualização fornecidos pelo cliente.
     * @param usuarioURL entidade existente que será modificada.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "urlMonitorada", ignore = true)
    fun updateEntityFromDTO(dto: UsuarioURLUpdateDTO, @MappingTarget usuarioURL: UsuarioURL)
}
