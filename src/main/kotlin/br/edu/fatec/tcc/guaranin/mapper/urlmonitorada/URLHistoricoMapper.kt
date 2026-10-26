package br.edu.fatec.tcc.guaranin.mapper.urlmonitorada

import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLHistoricoCreateDTO
import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLHistoricoResponseDTO
import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLHistoricoUpdateDTO
import br.edu.fatec.tcc.guaranin.model.URLHistorico
import org.mapstruct.*

/**
 * Mapper MapStruct para conversão entre a entidade [URLHistorico] e seus DTOs.
 *
 * A implementação é gerada em tempo de compilação pelo MapStruct via kapt e
 * registrada como bean Spring por meio de `componentModel = "spring"`.
 * Destinos sem correspondência na origem são ignorados
 * ([ReportingPolicy.IGNORE]) e propriedades nulas não sobrescrevem valores
 * existentes ([NullValuePropertyMappingStrategy.IGNORE]).
 *
 * @see URLHistorico
 * @see URLHistoricoCreateDTO
 * @see URLHistoricoUpdateDTO
 * @see URLHistoricoResponseDTO
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface URLHistoricoMapper {

    /**
     * Converte uma entidade [URLHistorico] persistida em
     * [URLHistoricoResponseDTO].
     *
     * @param urlHistorico entidade de origem.
     * @return DTO de saída pronto para serialização.
     */
    fun toResponseDTO(urlHistorico: URLHistorico): URLHistoricoResponseDTO

    /**
     * Converte um [URLHistoricoCreateDTO] em uma nova entidade [URLHistorico].
     *
     * O `id` é ignorado, pois é gerado pelo banco de dados na persistência.
     *
     * @param urlHistoricoCreateDTO dados de entrada para criação.
     * @return nova entidade ainda não persistida.
     */
    @Mapping(target = "id", ignore = true)
    fun toEntity(urlHistoricoCreateDTO: URLHistoricoCreateDTO): URLHistorico

    /**
     * Atualiza in-place uma entidade [URLHistorico] com os dados de
     * [URLHistoricoUpdateDTO].
     *
     * O `id` original é preservado e campos nulos no DTO mantêm o valor atual.
     *
     * @param urlHistoricoUpdateDTO dados de atualização fornecidos pelo cliente.
     * @param urlHistorico entidade existente que será modificada.
     */
    @Mapping(target = "id", ignore = true)
    fun updateEntityFromDTO(
        urlHistoricoUpdateDTO: URLHistoricoUpdateDTO,
        @MappingTarget urlHistorico: URLHistorico
    )
}