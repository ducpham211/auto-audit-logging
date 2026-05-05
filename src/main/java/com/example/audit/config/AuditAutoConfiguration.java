package com.example.audit.config;

import com.example.audit.aspect.AuditLoggingAspect;
import com.example.audit.provider.AuditUserProvider;
import com.example.audit.provider.DefaultAuditUserProvider;
import com.example.audit.provider.SecurityAuditUserProvider;
import com.example.audit.service.AuditLogService;
import com.example.audit.writer.AuditLogWriter;
import com.example.audit.writer.ConsoleAuditLogWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Cấu hình tự động cho Audit Logging Starter.
 * Sẽ được khởi tạo nếu người dùng đặt 'audit.enabled=true' trong application.properties/yml.
 */
@AutoConfiguration
@ConditionalOnProperty(name = "audit.enabled", havingValue = "true")
@EnableAspectJAutoProxy
@EnableAsync
public class AuditAutoConfiguration {

    // 0. Khởi tạo một ObjectMapper riêng rẽ chuyên biệt cho Audit Log
    // Để không ảnh hưởng/xung đột với ObjectMapper của dự án đích
    @Bean(name = "auditObjectMapper")
    public ObjectMapper auditObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        
        // Kích hoạt tính năng Data Masking
        SimpleModule maskingModule = new SimpleModule();
        maskingModule.setSerializerModifier(new MaskingBeanSerializerModifier());
        mapper.registerModule(maskingModule);

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return mapper;
    }

    // 1. Cấu hình ThreadPool riêng cho việc ghi log
    @Bean(name = "auditLogThreadPool")
    @ConditionalOnMissingBean(name = "auditLogThreadPool")
    public Executor auditLogThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("AuditLog-");
        executor.initialize();
        return executor;
    }

    // 2. Cấu hình AuditLogWriter mặc định (nếu dev không tự custom)
    @Bean
    @ConditionalOnMissingBean
    public AuditLogWriter consoleAuditLogWriter(@Qualifier("auditObjectMapper") ObjectMapper auditObjectMapper) {
        return new ConsoleAuditLogWriter(auditObjectMapper);
    }

    // 3. Cấu hình AuditUserProvider (Tự nhận diện có Spring Security hay không)
    @Bean
    @ConditionalOnClass(name = "org.springframework.security.core.context.SecurityContextHolder")
    @ConditionalOnMissingBean(AuditUserProvider.class)
    public AuditUserProvider securityAuditUserProvider() {
        return new SecurityAuditUserProvider();
    }

    @Bean
    @ConditionalOnMissingBean(AuditUserProvider.class)
    public AuditUserProvider defaultAuditUserProvider() {
        return new DefaultAuditUserProvider();
    }

    // 4. Khởi tạo Service
    @Bean
    public AuditLogService auditLogService(List<AuditLogWriter> writers) {
        return new AuditLogService(writers);
    }

    // 5. Khởi tạo Aspect
    @Bean
    public AuditLoggingAspect auditLoggingAspect(AuditLogService auditLogService, 
                                                 AuditUserProvider auditUserProvider, 
                                                 @Qualifier("auditObjectMapper") ObjectMapper auditObjectMapper) {
        return new AuditLoggingAspect(auditLogService, auditUserProvider, auditObjectMapper);
    }
}
