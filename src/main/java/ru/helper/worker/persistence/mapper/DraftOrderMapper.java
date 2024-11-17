package ru.helper.worker.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.helper.worker.business.create_order.process.context.OrderContext;
import ru.helper.worker.config.mapper.MapperConfiguration;
import ru.helper.worker.persistence.entity.DraftOrderEntity;

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
}
