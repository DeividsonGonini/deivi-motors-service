package com.deivimotors.config;

import com.deivimotors.adapters.out.CallbackPaymentAdapter;
import com.deivimotors.application.usecase.CallbackPaymentUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CallbackPaymentConfig {
    @Bean
    public CallbackPaymentUseCase callbackPaymentUseCase(
            CallbackPaymentAdapter callbackPaymentAdapter
    ){
        return new CallbackPaymentUseCase(callbackPaymentAdapter);
    }
}
