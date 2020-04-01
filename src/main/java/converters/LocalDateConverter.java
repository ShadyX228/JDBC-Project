package converters;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

import static webdebugger.WebInputDebugger.checkBirth;

public class LocalDateConverter implements Converter<String, LocalDate> {
    @Override
    public LocalDate convert(String birthday) {
        return checkBirth(birthday);
    }
}