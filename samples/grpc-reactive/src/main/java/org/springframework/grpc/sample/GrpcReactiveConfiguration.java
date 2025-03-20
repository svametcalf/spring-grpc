package org.springframework.grpc.sample;

import io.grpc.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.server.exception.GrpcExceptionHandler;

@Configuration
public class GrpcReactiveConfiguration {
    private static final Logger log = LoggerFactory.getLogger(GrpcReactiveConfiguration.class);

    @Bean
    GrpcExceptionHandler grpcExceptionHandler() {
        log.info("GrpcExceptionHandler");
        return ex -> {
            if(ex instanceof IllegalArgumentException ){
                log.error("Error in grpc exception", ex);
                return Status.INVALID_ARGUMENT.withDescription(ex.getMessage());
            }
            return Status.INTERNAL.withCause(ex).withDescription(ex.getMessage());
        };
    }
}
