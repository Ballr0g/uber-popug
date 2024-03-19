package org.uber.popug.employee.billing.mapping;

import org.mapstruct.Mapper;
import org.uber.popug.employee.billing.domain.billing.cycle.BillingCycle;
import org.uber.popug.employee.billing.entity.billing.cycle.BillingCycleEntity;

@Mapper
public interface BillingCyclesPersistenceMapper {

    BillingCycle toBusiness(BillingCycleEntity billingCycleEntity);

}
