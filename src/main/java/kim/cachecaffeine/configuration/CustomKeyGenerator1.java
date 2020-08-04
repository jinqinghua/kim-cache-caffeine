package kim.cachecaffeine.configuration;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

public class CustomKeyGenerator1 implements KeyGenerator {
    private static final String DELIMITER = "_";

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return target.getClass().getSimpleName() + DELIMITER
                + method.getName() + DELIMITER
                + StringUtils.arrayToDelimitedString(params, DELIMITER);
    }
}
