package com.example.audit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Đánh dấu các hàm cần được tự động ghi log (Audit Logging).
 * Aspect sẽ chặn (intercept) các hàm có annotation này để trích xuất thông tin.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoAudit {
    
    /**
     * Tên hành động (Action Name). Ví dụ: "CREATE_ORDER", "UPDATE_USER".
     * Nếu để trống, hệ thống sẽ sử dụng tên phương thức (Method name).
     */
    String action() default "";
    
}
