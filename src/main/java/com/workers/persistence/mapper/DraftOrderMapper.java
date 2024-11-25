package com.workers.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.workers.business.create_order.model.enums.OrderCategory;
import com.workers.business.create_order.process.context.OrderContext;
import com.workers.config.mapper.MapperConfiguration;
import com.workers.persistence.entity.DraftOrderEntity;
import com.workers.rest.create_order.model.OrderCreateRequest;

@Mapper(config = MapperConfiguration.class)
public interface DraftOrderMapper {

    @Mapping(target = "chatId", source = "chatId")
    @Mapping(target = "customerId", source = "orderRequest.customerId")
    @Mapping(target = "category", source = "orderRequest.category")
    @Mapping(target = "shortDescription", source = "orderRequest.shortDescription")
    @Mapping(target = "detailedDescription", source = "orderRequest.detailedDescription")
    @Mapping(target = "amount", source = "orderRequest.amount")
    @Mapping(target = "address", source = "orderRequest.address")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "sendProcess", ignore = true)
    DraftOrderEntity toEntity(OrderContext source);

    @Mapping(target = "draftId", source = "id")
    @Mapping(target = "category", source = "category", qualifiedByName = "getCategoryCode")
    OrderCreateRequest toRequest(DraftOrderEntity source);

    @Named("getCategoryCode")
    default Integer getCategoryCode(OrderCategory source) {
        return source.getCode();
    }
}
