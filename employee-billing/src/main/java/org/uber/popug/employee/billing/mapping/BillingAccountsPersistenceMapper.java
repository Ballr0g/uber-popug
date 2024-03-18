package org.uber.popug.employee.billing.mapping;

import org.mapstruct.Mapper;
import org.uber.popug.employee.billing.domain.billing.account.BillingAccount;
import org.uber.popug.employee.billing.entity.billing.account.BillingAccountEntity;

@Mapper
public interface BillingAccountsPersistenceMapper {

    BillingAccount toBusiness(BillingAccountEntity billingAccountEntity);

}
