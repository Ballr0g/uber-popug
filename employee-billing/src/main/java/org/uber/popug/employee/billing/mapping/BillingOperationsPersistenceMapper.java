package org.uber.popug.employee.billing.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.uber.popug.employee.billing.domain.aggregates.BillingOperationFullData;
import org.uber.popug.employee.billing.entity.billing.operation.BillingOperationEntity;

@Mapper
public interface BillingOperationsPersistenceMapper {

    @Mapping(source = "data.id", target = "id")
    @Mapping(source = "data.publicId", target = "publicId")
    @Mapping(source = "ownerUser.id", target = "ownerUserId")
    @Mapping(source = "data.description", target = "description")
    @Mapping(source = "data.paymentData.credit", target = "credit")
    @Mapping(source = "data.paymentData.debit", target = "debit")
    @Mapping(source = "ownerCycle.id", target = "billingCycleId")
    BillingOperationEntity fromBusiness(BillingOperationFullData billingOperation);

}
