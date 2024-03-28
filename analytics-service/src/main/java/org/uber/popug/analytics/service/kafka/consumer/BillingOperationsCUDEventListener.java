package org.uber.popug.analytics.service.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.uber.popug.analytics.service.kafka.generated.dto.BillingOperationCreatedEventV1;

@Service
@RequiredArgsConstructor
@KafkaListener(
        topics = "${kafka.listener.billing-operations-stream.topics}",
        groupId = "${kafka.listener.billing-operations-stream.group-id}",
        containerFactory = "billingOperationsCUDListenerContainerFactory"
)
public class BillingOperationsCUDEventListener {

    @KafkaHandler
    public void handleTaskCreatedCUDEvent(BillingOperationCreatedEventV1 billingOperationCreatedEventV1) {
        // Todo: actual ClickHouse-based analytics and adequate logger.
        System.out.println(("--> Received billing operation: %s"
                .formatted(billingOperationCreatedEventV1.toString())));
    }

}
