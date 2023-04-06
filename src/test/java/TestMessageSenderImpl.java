import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class TestMessageSenderImpl {
    @ParameterizedTest
    @MethodSource("getIp")
    public void testSend(String ip, Location location, String message, String expected) {
        //arrange
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp(ip))
                .thenReturn(location);

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(location.getCountry())).thenReturn(message);

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> headers = new HashMap<>();
        headers.put("x-real-ip", ip);
        //act
        String actual = messageSender.send(headers);
        //assert
        assertThat(actual, equalTo(expected));
    }

    public static Stream<Arguments> getIp() {
        return Stream.of(
                Arguments.of("172.0.32.11"
                        , new Location("Moscow", Country.RUSSIA, "Lenina", 15)
                        , "Добро пожаловать"
                        , "Добро пожаловать"),
                Arguments.of("96.44.183.149"
                        , new Location("New York", Country.USA, " 10th Avenue", 32)
                        , "Welcome"
                        , "Welcome")
        );
    }
}
