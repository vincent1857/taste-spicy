package cn.vincent.taste.spicy.web.convert;

import org.springframework.http.converter.HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vincent
 * @version 1.0 2020/3/11 19:09
 */
public class HttpMessageConverterContainer {

    private static final List<HttpMessageConverter<?>> CONVERTERS = new ArrayList<>();

    public static void addAll(List<HttpMessageConverter<?>> converters) {
        if (converters != null && converters.size() > 0) {
            HttpMessageConverterContainer.CONVERTERS.addAll(converters);
        }
    }

    public static void addAll(int index, List<HttpMessageConverter<?>> converters) {
        if (converters != null && converters.size() > 0) {
            HttpMessageConverterContainer.CONVERTERS.addAll(index, converters);
        }
    }

    public static void add(HttpMessageConverter<?> converter) {
        if (converter != null) {
            HttpMessageConverterContainer.CONVERTERS.add(converter);
        }
    }

    public static void add(int index, HttpMessageConverter<?> converter) {
        if (converter != null) {
            HttpMessageConverterContainer.CONVERTERS.add(index, converter);
        }
    }

    public static void reset(List<HttpMessageConverter<?>> converters) {
        HttpMessageConverterContainer.CONVERTERS.clear();
        addAll(converters);
    }

    public static List<HttpMessageConverter<?>> getConverters() {
        return HttpMessageConverterContainer.CONVERTERS;
    }
}
