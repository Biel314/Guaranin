package br.edu.fatec.tcc.guaranin.mapper.aplicativomonitorado

import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoHistoricoResponseDTO
import br.edu.fatec.tcc.guaranin.model.AplicativoHistorico
import org.mapstruct.*


/**
 * Mapper MapStruct para conversão entre a entidade
 * [AplicativoHistorico] e seus DTOs.
 *
 * A implementação é gerada automaticamente pelo MapStruct e registrada
 * como bean Spring através de `componentModel = "spring"`.
 *
 * Destinos sem correspondência são ignorados
 * ([ReportingPolicy.IGNORE]) e propriedades nulas não sobrescrevem
 * valores existentes
 * ([NullValuePropertyMappingStrategy.IGNORE]).
 *
 * @see AplicativoHistorico
 * @see AplicativoHistoricoResponseDTO
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface AplicativoHistoricoMapper {

    /**
     * Converte uma entidade [AplicativoHistorico] persistida em um
     * [AplicativoHistoricoResponseDTO].
     *
     * O identificador da associação [UsuarioAplicativo] é extraído da
     * entidade relacionada através do atributo
     * `usuarioAplicativo.id`.
     *
     * @param aplicativoHistorico entidade de origem.
     * @return DTO de saída correspondente.
     */
    @Mapping(
        target = "usuarioAplicativoId",
        source = "usuarioAplicativo.id"
    )
    fun toResponseDTO(
        aplicativoHistorico: AplicativoHistorico
    ): AplicativoHistoricoResponseDTO
}