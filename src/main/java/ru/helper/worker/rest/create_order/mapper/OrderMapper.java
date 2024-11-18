package ru.helper.worker.rest.create_order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.helper.worker.config.mapper.MapperConfiguration;
import ru.helper.worker.business.create_order.model.OrderRequest;
import ru.helper.worker.business.create_order.model.enums.OrderCategory;
import ru.helper.worker.rest.create_order.model.OrderCreateRequestDto;

@Mapper(config = MapperConfiguration.class)
public interface OrderMapper {

    @Mapping(target = "category", source = "category", qualifiedByName = "getCategoryCode")
    @Mapping(target = "draftId", ignore = true)
    OrderCreateRequestDto toRequest(OrderRequest source);

    @Named("getCategoryCode")
    default Integer getCategoryCode(OrderCategory source) {
        return source.getCode();
    }
}
