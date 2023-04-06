import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.beans.HasProperty.*;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.Matchers.*;

import java.util.stream.Stream;

public class TestGeoServiceImpl {
    public GeoServiceImpl geoService;

    @BeforeEach
    public void beforeEach() {
        geoService = new GeoServiceImpl();
    }

    @ParameterizedTest
    @MethodSource("ipCountry")
    public void testByIp(String ip, Country expected) {
        //arrange

        //act
        Location actual = geoService.byIp(ip);
        //assert
        assertThat(actual, hasProperty("country", equalTo(expected)));
    }

    public static Stream<Arguments> ipCountry() {
        return Stream.of(
                Arguments.of("127.0.0.1", null),
                Arguments.of("172.0.32.11", Country.RUSSIA),
                Arguments.of("96.44.183.149", Country.USA),
                Arguments.of("172.0.32.12", Country.RUSSIA),
                Arguments.of("96.44.183.150", Country.USA)
        );
    }

    @Test
    public void testByIpNullReturn() {
        //arrange
        //act
        Location actual = geoService.byIp("93.44.183.150");
        //assert
        assertThat(actual, nullValue(Location.class));
    }

    @Test
    public void testByCoordinates() {
        //arrange
        Class<RuntimeException> expectedType = RuntimeException.class;
        //act
        Executable executable = () -> geoService.byCoordinates(1.0, 1.0);
        //assert
        Assertions.assertThrowsExactly(expectedType, executable);
    }
}
