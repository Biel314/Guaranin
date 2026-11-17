package br.edu.fatec.tcc.guaranin.mapper.usuario

import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioAplicativoResponseDTO
import br.edu.fatec.tcc.guaranin.mapper.aplicativomonitorado.AplicativoMonitoradoMapper
import br.edu.fatec.tcc.guaranin.model.UsuarioAplicativo
import org.mapstruct.*


/**
 * Mapper MapStruct para conversão entre a entidade [UsuarioAplicativo]
 * e seus DTOs de saída.
 *
 * A implementação é gerada automaticamente pelo MapStruct e registrada
 * como bean Spring através de `componentModel = "spring"`.
 *
 * Este mapper utiliza [AplicativoMonitoradoMapper] para converter a
 * entidade relacionada [AplicativoMonitorado] para seu respectivo DTO.
 *
 * Destinos sem correspondência são ignorados
 * ([ReportingPolicy.IGNORE]) e propriedades nulas não sobrescrevem
 * valores existentes
 * ([NullValuePropertyMappingStrategy.IGNORE]).
 *
 * @see UsuarioAplicativo
 * @see UsuarioAplicativoResponseDTO
 * @see AplicativoMonitoradoMapper
 */
@Mapper(
    componentModel = "spring",
    uses = [AplicativoMonitoradoMapper::class],
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface UsuarioAplicativoMapper {

    /**
     * Converte uma entidade [UsuarioAplicativo] persistida em um
     * [UsuarioAplicativoResponseDTO].
     *
     * O aplicativo monitorado associado é convertido automaticamente
     * através do [AplicativoMonitoradoMapper].
     *
     * @param usuarioAplicativo entidade de origem.
     * @return DTO de saída correspondente.
     */
    fun toResponseDTO(
        usuarioAplicativo: UsuarioAplicativo
    ): UsuarioAplicativoResponseDTO
}