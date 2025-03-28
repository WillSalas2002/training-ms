package com.epam.training.logging;

import com.epam.training.util.TransactionContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler
    ) {
        String transactionId = TransactionContext.generateTransactionId();
        TransactionContext.setTransactionId(transactionId);

        log.info("Transaction ID: {}, Incoming Request: {} {}",
                transactionId, request.getMethod(), request.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex
    ) {
        log.info("Transaction ID: {}, Response Status: {}",
                TransactionContext.getTransactionId(), response.getStatus());
        TransactionContext.clear();
    }
}
