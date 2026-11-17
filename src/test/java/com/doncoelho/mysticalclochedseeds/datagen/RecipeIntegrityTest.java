
package com.doncoelho.mysticalclochedseeds.datagen;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static org.junit.jupiter.api.Assertions.*;

class RecipeIntegrityTest {

    private static final Path RECIPE_DIRECTORY = Path.of(
            "src/generated/resources/data/mysticalclochedseeds/recipe"
    );


    @Test
    void allGeneratedRecipesShouldHaveValidJsonStructure()
            throws IOException {

        assertTrue(
                Files.isDirectory(RECIPE_DIRECTORY),
                "Generated recipe directory was not found."
        );

        List<Path> recipes;

        try (Stream<Path> files = Files.list(RECIPE_DIRECTORY)) {
            recipes = files
                    .filter(path -> path.toString().endsWith(".json"))
                    .toList();
        }

        assertFalse(recipes.isEmpty(), "No recipes found.");

        for (Path recipe : recipes) {

            JsonObject json = assertDoesNotThrow(() -> {
                try (var reader = Files.newBufferedReader(recipe)) {
                    return JsonParser.parseReader(reader).getAsJsonObject();
                }
            }, "Invalid JSON: " + recipe);

            assertAll(recipe.getFileName().toString(),

                    () -> assertEquals(
                            "immersiveengineering:cloche",
                            json.get("type").getAsString(),
                            "Incorrect recipe type"
                    ),

                    () -> assertTrue(
                            json.has("input")
                                    && json.getAsJsonObject("input").has("item"),
                            "Missing seed input"
                    ),

                    () -> assertTrue(
                            json.has("soil")
                                    && json.getAsJsonObject("soil").has("item"),
                            "Missing soil input"
                    ),

                    () -> assertTrue(
                            json.has("results")
                                    && json.getAsJsonArray("results").size() > 0,
                            "Missing recipe outputs"
                    ),

                    () -> assertTrue(
                            json.has("time")
                                    && json.get("time").getAsInt() > 0,
                            "Invalid growth time"
                    ),

                    () -> assertTrue(
                            json.has("render")
                                    && json.getAsJsonObject("render").has("type"),
                            "Missing render configuration"
                    )
            );
        }
    }


    @Test
    void insaniumRecipesShouldRequireMysticalAgradditions()
            throws IOException {

        assertTrue(
                Files.isDirectory(RECIPE_DIRECTORY),
                "Generated recipe directory was not found."
        );

        List<Path> recipes;

        try (Stream<Path> files = Files.list(RECIPE_DIRECTORY)) {
            recipes = files
                    .filter(path -> path.toString().endsWith(".json"))
                    .toList();
        }

        List<Path> vanillaRecipes = recipes.stream()
                .filter(path -> path.getFileName()
                        .toString().endsWith("_from_vanilla.json"))
                .toList();

        List<Path> insaniumRecipes = recipes.stream()
                .filter(path -> path.getFileName()
                        .toString().endsWith("_from_insanium.json"))
                .toList();

        assertFalse(
                vanillaRecipes.isEmpty(),
                "No vanilla soil recipes were found."
        );

        assertEquals(
                vanillaRecipes.size(),
                insaniumRecipes.size(),
                "Every supported crop should have an Insanium recipe."
        );

        for (Path recipe : insaniumRecipes) {
            String json = Files.readString(recipe);
            String name = recipe.getFileName().toString();

            assertAll(name,
                    () -> assertTrue(
                            json.contains("\"neoforge:conditions\""),
                            "Missing NeoForge conditions"
                    ),
                    () -> assertTrue(
                            json.contains("\"type\": \"neoforge:mod_loaded\""),
                            "Missing mod_loaded condition"
                    ),
                    () -> assertTrue(
                            json.contains("\"modid\": \"mysticalagradditions\""),
                            "Missing Mystical Agradditions requirement"
                    ),
                    () -> assertTrue(
                            json.contains("mysticalagradditions:insanium_farmland"),
                            "Incorrect farmland item"
                    )
            );
        }
    }


    @Test
    void tierSixCropsShouldNotHaveClocheRecipesYet()
            throws IOException {

        assertTrue(
                Files.isDirectory(RECIPE_DIRECTORY),
                "Generated recipe directory was not found."
        );

        List<String> tierSixCrops = List.of(
                "nether_star",
                "dragon_egg",
                "gaia_spirit",
                "awakened_draconium",
                "neutronium",
                "nitro_crystal"
        );

        List<String> recipeNames;

        try (Stream<Path> files = Files.list(RECIPE_DIRECTORY)) {
            recipeNames = files
                    .map(path -> path.getFileName().toString())
                    .filter(name -> name.endsWith(".json"))
                    .toList();
        }

        for (String crop : tierSixCrops) {
            String prefix = "cloche_" + crop + "_from_";

            assertFalse(
                    recipeNames.stream()
                            .anyMatch(name -> name.startsWith(prefix)),
                    "Unexpected Tier 6 recipe generated for: " + crop
            );
        }
    }


    @Test
    void packagedJarShouldContainEveryGeneratedRecipe()
            throws IOException {

        String version = System.getProperty("modVersion");

        assertNotNull(
                version,
                "Gradle did not provide the mod version."
        );

        Path jarPath = Path.of(
                "build", "libs",
                "mysticalclochedseeds-" + version + ".jar"
        );

        assertTrue(
                Files.isRegularFile(jarPath),
                "Mod JAR was not found: " + jarPath
        );

        assertTrue(
                Files.isDirectory(RECIPE_DIRECTORY),
                "Generated recipe directory was not found."
        );

        Set<String> expectedRecipes;

        try (Stream<Path> files = Files.list(RECIPE_DIRECTORY)) {
            expectedRecipes = files
                    .filter(path -> path.toString().endsWith(".json"))
                    .map(path ->
                            "data/mysticalclochedseeds/recipe/"
                                    + path.getFileName()
                    )
                    .collect(Collectors.toSet());
        }

        assertFalse(
                expectedRecipes.isEmpty(),
                "No generated recipes were found."
        );

        try (ZipFile jar = new ZipFile(jarPath.toFile())) {

            Set<String> packagedRecipes = jar.stream()
                    .map(ZipEntry::getName)
                    .filter(name -> name.startsWith(
                            "data/mysticalclochedseeds/recipe/"
                    ))
                    .filter(name -> name.endsWith(".json"))
                    .collect(Collectors.toSet());

            assertEquals(
                    expectedRecipes,
                    packagedRecipes,
                    "Packaged recipes differ from generated recipes."
            );

            assertNotNull(
                    jar.getEntry(
                            "assets/mysticalclochedseeds/textures/"
                                    + "block/farmland_moist_insanium.png"
                    ),
                    "Insanium texture is missing from the JAR."
            );

            assertNotNull(
                    jar.getEntry("META-INF/neoforge.mods.toml"),
                    "NeoForge metadata is missing from the JAR."
            );
        }
    }

}
