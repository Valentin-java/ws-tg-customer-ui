package com.workers.rest.create_order.mapper;

import com.workers.business.create_order.model.enums.OrderCategory;
import com.workers.rest.create_order.model.OrderCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.workers.config.mapper.MapperConfiguration;
import com.workers.business.create_order.model.OrderRequest;

@Mapper(config = MapperConfiguration.class)
public interface OrderMapper {

    @Mapping(target = "category", source = "category", qualifiedByName = "getCategoryCode")
    @Mapping(target = "draftId", ignore = true)
    OrderCreateRequest toRequest(OrderRequest source);

    @Named("getCategoryCode")
    default Integer getCategoryCode(OrderCategory source) {
        return source.getCode();
    }
}
