package ru.helper.worker.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.helper.worker.business.create_order.model.enums.OrderCategory;
import ru.helper.worker.business.create_order.process.context.OrderContext;
import ru.helper.worker.config.mapper.MapperConfiguration;
import ru.helper.worker.persistence.entity.DraftOrderEntity;
import ru.helper.worker.rest.external.create_order.model.OrderCreateRequest;

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
