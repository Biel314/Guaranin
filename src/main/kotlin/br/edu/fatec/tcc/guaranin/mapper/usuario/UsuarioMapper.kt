package br.edu.fatec.tcc.guaranin.mapper.usuario

import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioCreateDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioResponseDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioUpdateDTO
import br.edu.fatec.tcc.guaranin.model.Usuario
import org.mapstruct.*

/**
 * Mapper MapStruct para conversão entre a entidade [Usuario] e seus DTOs.
 *
 * A implementação é gerada em tempo de compilação pelo MapStruct via kapt e
 * registrada como bean Spring por `componentModel = "spring"`. Destinos sem
 * correspondência na origem são ignorados ([ReportingPolicy.IGNORE]) e
 * propriedades nulas não sobrescrevem valores existentes
 * ([NullValuePropertyMappingStrategy.IGNORE]).
 *
 * @see Usuario
 * @see UsuarioCreateDTO
 * @see UsuarioUpdateDTO
 * @see UsuarioResponseDTO
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface UsuarioMapper {

    /**
     * Converte uma entidade [Usuario] persistida em um [UsuarioResponseDTO],
     * adequado para ser exposto por endpoints da API (sem dados sensíveis
     * como a senha).
     *
     * @param usuario entidade de origem, já persistida no banco de dados.
     * @return DTO de saída pronto para serialização.
     */
    fun toResponseDTO(usuario: Usuario): UsuarioResponseDTO

    /**
     * Converte uma entidade [Usuario] persistida em um [UsuarioUpdateDTO],
     * utilizado para preencher o formulário de edição.
     *
     * @param usuario entidade de origem.
     * @return DTO de atualização correspondente.
     */
    fun toUpdateDTO(usuario: Usuario): UsuarioUpdateDTO

    /**
     * Converte um [UsuarioCreateDTO], recebido na requisição de cadastro,
     * em uma nova entidade [Usuario].
     *
     * O campo `id` é ignorado no mapeamento, pois é gerado pelo banco de
     * dados na persistência. A senha mapeada aqui está em texto plano —
     * cabe à camada de serviço aplicar o hash (`PasswordEncoder`) antes
     * de salvar a entidade.
     *
     * @param usuarioCreateDTO dados de entrada para criação do usuário.
     * @return nova instância de [Usuario], ainda não persistida.
     */
    @Mapping(target = "id", ignore = true)
    fun toEntity(usuarioCreateDTO: UsuarioCreateDTO): Usuario

    /**
     * Converte um [UsuarioUpdateDTO] em uma entidade [Usuario].
     *
     * @param usuarioUpdateDTO dados de atualização de usuário.
     * @return nova instância de [Usuario].
     */
    fun toEntity(usuarioUpdateDTO: UsuarioUpdateDTO): Usuario

    /**
     * Atualiza os campos de uma entidade [Usuario] existente com os valores
     * fornecidos em um [UsuarioUpdateDTO].
     *
     * O parâmetro `usuario` é modificado in-place (anotado com
     * [MappingTarget]), e o `id` original é preservado, já que é ignorado
     * no mapeamento.
     *
     * @param dto dados de atualização fornecidos pelo cliente.
     * @param usuario entidade existente a ser atualizada.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    fun updateEntityFromDTO(dto: UsuarioUpdateDTO, @MappingTarget usuario: Usuario)


}