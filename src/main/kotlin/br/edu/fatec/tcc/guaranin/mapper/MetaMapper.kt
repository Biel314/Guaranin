package br.edu.fatec.tcc.guaranin.mapper

import org.mapstruct.Mapper
import org.mapstruct.NullValuePropertyMappingStrategy
import org.mapstruct.ReportingPolicy
import br.edu.fatec.tcc.guaranin.model.Meta
import br.edu.fatec.tcc.guaranin.dto.MetaCreateDTO
import br.edu.fatec.tcc.guaranin.dto.MetaResponseDTO
import br.edu.fatec.tcc.guaranin.dto.MetaUpdateDTO
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface MetaMapper {

    fun toResponseDTO(meta: Meta): MetaResponseDTO

    fun toUpdateDTO(meta: Meta): MetaUpdateDTO

    @Mapping(target = "id", ignore = true)
    fun toEntity(metaCreateDTO: MetaCreateDTO): Meta

    fun toEntity(metaUpdateDTO: MetaUpdateDTO): Meta

    @Mapping(target = "id", ignore = true)
    fun updateEntityFromDTO(dto: MetaUpdateDTO, @MappingTarget meta: Meta)
}