package org.uber.popug.employee.billing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class EmployeeBillingApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeBillingApplication.class, args);
    }

}
