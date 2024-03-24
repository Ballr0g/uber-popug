package org.uber.popug.employee.billing.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uber.popug.employee.billing.service.BillingCycleClosingService;
import org.uber.popug.employee.billing.service.PaymentService;

@Service
@RequiredArgsConstructor
public class BillingCycleManagementScheduler {

    private final BillingCycleClosingService billingCycleClosingService;
    private final PaymentService paymentService;

    // Todo: Add email notification.
    @Transactional
    @Scheduled(cron = "${billing-cycles.closing-interval}", zone = "UTC")
    public void closeCurrentBillingCycle() {
        billingCycleClosingService.closeActiveBillingCycle();
        paymentService.payUsersForPositiveBalance();
    }

}
