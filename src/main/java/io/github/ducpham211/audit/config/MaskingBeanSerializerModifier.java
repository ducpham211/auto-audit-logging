package io.github.ducpham211.audit.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.io.IOException;
import java.util.List;

/**
 * Jackson Modifier giúp phát hiện và che giấu (mask) dữ liệu nhạy cảm
 * khi chuyển đổi Object thành chuỗi JSON.
 */
public class MaskingBeanSerializerModifier extends BeanSerializerModifier {

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                     BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {
        for (BeanPropertyWriter writer : beanProperties) {
            String name = writer.getName().toLowerCase();
            // Nếu field name chứa các từ khóa nhạy cảm
            if (name.contains("password") || name.contains("token") ||
                name.contains("secret") || name.contains("creditcard") ||
                name.contains("cvv")) {
                
                // Gán một Serializer giả mạo để ghi đè giá trị thực bằng [MASKED]
                writer.assignSerializer(new JsonSerializer<Object>() {
                    @Override
                    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        if (value == null) {
                            gen.writeNull();
                        } else {
                            gen.writeString("[MASKED]");
                        }
                    }
                });
            }
        }
        return beanProperties;
    }
}

