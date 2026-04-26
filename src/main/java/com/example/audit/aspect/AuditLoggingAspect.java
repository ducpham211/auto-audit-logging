package com.example.audit.aspect;

import com.example.audit.annotation.AutoAudit;
import com.example.audit.model.AuditLog;
import com.example.audit.provider.AuditUserProvider;
import com.example.audit.service.AuditLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Aspect cốt lõi để chặn các hàm được đánh dấu @AutoAudit và ghi log.
 */
@Aspect
public class AuditLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuditLoggingAspect.class);

    private final AuditLogService auditLogService;
    private final AuditUserProvider auditUserProvider;
    private final ObjectMapper objectMapper;

    public AuditLoggingAspect(AuditLogService auditLogService, AuditUserProvider auditUserProvider, ObjectMapper objectMapper) {
        this.auditLogService = auditLogService;
        this.auditUserProvider = auditUserProvider;
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(com.example.audit.annotation.AutoAudit)")
    public Object logAction(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        AuditLog auditLog = new AuditLog();
        auditLog.setId(UUID.randomUUID().toString());

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AutoAudit autoAudit = method.getAnnotation(AutoAudit.class);

        // Lấy thông tin cơ bản
        auditLog.setClassName(signature.getDeclaringTypeName());
        auditLog.setMethodName(signature.getName());
        auditLog.setAction(autoAudit.action().isEmpty() ? signature.getName() : autoAudit.action());
        auditLog.setUsername(auditUserProvider.getCurrentUsername());

        // Lấy tham số (serialize ra JSON)
        try {
            auditLog.setArguments(objectMapper.writeValueAsString(joinPoint.getArgs()));
        } catch (JsonProcessingException e) {
            logger.warn("Failed to serialize arguments for audit log", e);
            auditLog.setArguments("N/A - Serialization Failed");
        }

        Object result = null;
        try {
            // Thực thi hàm gốc
            result = joinPoint.proceed();
            
            auditLog.setStatus("SUCCESS");
            try {
                auditLog.setResult(result != null ? objectMapper.writeValueAsString(result) : "null");
            } catch (JsonProcessingException e) {
                auditLog.setResult("N/A - Serialization Failed");
            }
            
            return result;
        } catch (Throwable e) {
            auditLog.setStatus("FAILED");
            auditLog.setErrorMessage(e.getMessage());
            throw e; // Ném lỗi ra cho luồng chính xử lý tiếp
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            auditLog.setExecutionTimeMs(executionTime);
            
            // Đẩy vào hàng đợi bất đồng bộ
            auditLogService.processLog(auditLog);
        }
    }
}
