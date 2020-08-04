package kim.cachecaffeine.configuration;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKey;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomKeyGenerator0 implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {

        List<Object> list = new ArrayList<>();
        list.add(target);
        list.add(method);
        if (null != params) {
            list.addAll(Arrays.asList(params));
        }
        return new SimpleKey(list.stream().toArray(Object[]::new));
    }
}
