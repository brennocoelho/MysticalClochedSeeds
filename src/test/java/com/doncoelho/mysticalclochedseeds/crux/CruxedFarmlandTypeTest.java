
package com.doncoelho.mysticalclochedseeds.crux;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CruxedFarmlandTypeTest {

    private long countAvailable(Set<String> mods) {
        return Arrays.stream(CruxedFarmlandType.values())
                .filter(type -> type.isAvailable(mods::contains))
                .count();
    }

    @Test
    void noAgradditionsShouldRegisterZeroFarmlands() {
        assertEquals(0, countAvailable(Set.of()));
    }

    @Test
    void unrelatedModsShouldNotEnableFarmlands() {
        assertEquals(0, countAvailable(Set.of(
                "botania",
                "draconicevolution",
                "avaritia",
                "powah"
        )));
    }

    @Test
    void agradditionsAloneShouldEnableTwoFarmlands() {
        assertEquals(2, countAvailable(Set.of(
                "mysticalagradditions"
        )));
    }

    @Test
    void agradditionsAndBotaniaShouldEnableThree() {
        assertEquals(3, countAvailable(Set.of(
                "mysticalagradditions",
                "botania"
        )));
    }

    @Test
    void allIntegrationsShouldEnableSixFarmlands() {
        assertEquals(6, countAvailable(Set.of(
                "mysticalagradditions",
                "botania",
                "draconicevolution",
                "avaritia",
                "powah"
        )));
    }

    @Test
    void shouldFindFarmlandTypeByCropName() {
        assertEquals(
                CruxedFarmlandType.NETHER_STAR,
                CruxedFarmlandType.fromCropName("nether_star")
        );

        assertNull(
                CruxedFarmlandType.fromCropName("unknown_crop")
        );
    }
}
