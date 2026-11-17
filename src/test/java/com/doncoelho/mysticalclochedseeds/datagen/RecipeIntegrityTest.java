
package com.doncoelho.mysticalclochedseeds.datagen;

import com.doncoelho.mysticalclochedseeds.tier.ClocheCalculator;

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
import java.util.Map;
import java.util.HashMap;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static org.junit.jupiter.api.Assertions.*;

class RecipeIntegrityTest {

    private static final Path RECIPE_DIRECTORY = Path.of(
            "src/generated/resources/data/mysticalclochedseeds/recipe"
    );

    private static final Map<String, Integer> INFERIUM_FARMLAND_TIERS = Map.of(
            "vanilla", 0,
            "inferium", 1,
            "prudentium", 2,
            "tertium", 3,
            "imperium", 4,
            "supremium", 5,
            "insanium", 6
    );

    private static JsonObject readRecipe(String filename)
            throws IOException {

        Path recipePath = RECIPE_DIRECTORY.resolve(filename);

        assertTrue(
                Files.isRegularFile(recipePath),
                "Recipe not found: " + filename
        );

        try (var reader = Files.newBufferedReader(recipePath)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

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

    @Test
    void inferiumRecipeGrowthTimesShouldMatchCalculator()
            throws IOException {

        for (var entry : INFERIUM_FARMLAND_TIERS.entrySet()) {

            String filename = "cloche_inferium_from_"
                    + entry.getKey() + ".json";

            JsonObject recipe = readRecipe(filename);

            int expectedTime = ClocheCalculator.calculateTime(
                    1,
                    entry.getValue()
            );

            int actualTime = recipe.get("time").getAsInt();

            assertEquals(
                    expectedTime,
                    actualTime,
                    "Incorrect growth time in " + filename
            );
        }
    }

    @Test
    void inferiumRecipeOutputsShouldMatchCalculator()
            throws IOException {

        for (var entry : INFERIUM_FARMLAND_TIERS.entrySet()) {

            String filename = "cloche_inferium_from_"
                    + entry.getKey() + ".json";

            JsonObject recipe = readRecipe(filename);

            var expected = ClocheCalculator.calculateInferiumOutput(
                    1,
                    entry.getValue()
            );

            int guaranteed = 0;
            float extraChance = 0.0f;

            for (var element : recipe.getAsJsonArray("results")) {

                JsonObject result = element.getAsJsonObject();

                // Guaranteed Inferium Essence
                if (result.has("id")
                        && "mysticalagriculture:inferium_essence"
                        .equals(result.get("id").getAsString())) {

                    guaranteed += result.has("count")
                            ? result.get("count").getAsInt()
                            : 1;
                }

                // Additional Inferium Essence with probability
                if (result.has("output")
                        && result.get("output").isJsonObject()) {

                    JsonObject output = result.getAsJsonObject("output");

                    if (output.has("id")
                            && "mysticalagriculture:inferium_essence"
                            .equals(output.get("id").getAsString())) {

                        extraChance += result.get("chance").getAsFloat();
                    }
                }
            }

            assertEquals(
                    expected.guaranteed(),
                    guaranteed,
                    "Incorrect guaranteed Inferium output in " + filename
            );

            assertEquals(
                    expected.extraChance(),
                    extraChance,
                    0.0001f,
                    "Incorrect additional Inferium chance in " + filename
            );
        }
    }

    @Test
    void inferiumRecipeSeedChancesShouldMatchCalculator()
            throws IOException {

        for (var entry : INFERIUM_FARMLAND_TIERS.entrySet()) {

            String filename = "cloche_inferium_from_"
                    + entry.getKey() + ".json";

            JsonObject recipe = readRecipe(filename);

            float actualChance = 0.0f;

            for (var element : recipe.getAsJsonArray("results")) {

                JsonObject result = element.getAsJsonObject();

                if (!result.has("output")) {
                    continue;
                }

                JsonObject output = result.getAsJsonObject("output");

                if (output.has("id")
                        && "mysticalagriculture:inferium_seeds"
                        .equals(output.get("id").getAsString())) {

                    assertTrue(
                            result.has("chance"),
                            "Missing seed drop chance in " + filename
                    );

                    actualChance += result.get("chance").getAsFloat();
                }
            }

            float expectedChance =
                    ClocheCalculator.calculateSeedDropChance(
                            1,
                            entry.getValue()
                    );

            assertEquals(
                    expectedChance,
                    actualChance,
                    0.0001f,
                    "Incorrect seed drop chance in " + filename
            );
        }
    }

    @Test
    void inferiumRecipeFertilizedEssenceChancesShouldMatchCalculator()
            throws IOException {

        for (var entry : INFERIUM_FARMLAND_TIERS.entrySet()) {

            String filename = "cloche_inferium_from_"
                    + entry.getKey() + ".json";

            JsonObject recipe = readRecipe(filename);

            int fertilizedOutputs = 0;
            float actualChance = 0.0f;

            for (var element : recipe.getAsJsonArray("results")) {

                JsonObject result = element.getAsJsonObject();

                if (!result.has("output")
                        || !result.get("output").isJsonObject()) {
                    continue;
                }

                JsonObject output = result.getAsJsonObject("output");

                if (output.has("id")
                        && "mysticalagriculture:fertilized_essence"
                        .equals(output.get("id").getAsString())) {

                    fertilizedOutputs++;

                    assertTrue(
                            result.has("chance"),
                            "Missing Fertilized Essence chance in " + filename
                    );

                    actualChance += result.get("chance").getAsFloat();
                }
            }

            assertEquals(
                    1,
                    fertilizedOutputs,
                    "Expected exactly one Fertilized Essence output in "
                            + filename
            );

            assertEquals(
                    ClocheCalculator.fertilizedChance(),
                    actualChance,
                    0.0001f,
                    "Incorrect Fertilized Essence chance in " + filename
            );
        }
    }

    @Test
    void allRecipesShouldHaveCorrectFertilizedEssenceChance()
            throws IOException {

        List<Path> recipes;

        try (Stream<Path> files = Files.list(RECIPE_DIRECTORY)) {
            recipes = files
                    .filter(path -> path.toString().endsWith(".json"))
                    .toList();
        }

        assertFalse(recipes.isEmpty(), "No recipes found.");

        for (Path path : recipes) {

            String filename = path.getFileName().toString();
            JsonObject recipe = readRecipe(filename);

            int fertilizedOutputs = 0;

            for (var element : recipe.getAsJsonArray("results")) {

                JsonObject result = element.getAsJsonObject();

                if (!result.has("output")
                        || !result.get("output").isJsonObject()) {
                    continue;
                }

                JsonObject output = result.getAsJsonObject("output");

                if (!output.has("id")
                        || !"mysticalagriculture:fertilized_essence"
                        .equals(output.get("id").getAsString())) {
                    continue;
                }

                fertilizedOutputs++;

                assertTrue(
                        result.has("chance"),
                        "Missing Fertilized Essence chance in " + filename
                );

                assertEquals(
                        ClocheCalculator.fertilizedChance(),
                        result.get("chance").getAsFloat(),
                        0.0001f,
                        "Incorrect Fertilized Essence chance in " + filename
                );
            }

            assertEquals(
                    1,
                    fertilizedOutputs,
                    "Expected exactly one Fertilized Essence output in "
                            + filename
            );
        }
    }

    @Test
    void allRecipesShouldHaveConsistentSeedDropChances()
            throws IOException {

        List<Path> recipes;

        try (Stream<Path> files = Files.list(RECIPE_DIRECTORY)) {
            recipes = files
                    .filter(path -> path.toString().endsWith(".json"))
                    .toList();
        }

        assertFalse(recipes.isEmpty(), "No recipes found.");

        Map<String, Integer> matchingTierRecipes = new HashMap<>();

        for (Path path : recipes) {

            String filename = path.getFileName().toString();
            JsonObject recipe = readRecipe(filename);

            String seedId = recipe.getAsJsonObject("input")
                    .get("item").getAsString();

            boolean vanilla = filename.endsWith("_from_vanilla.json");

            matchingTierRecipes.putIfAbsent(seedId, 0);

            int seedOutputs = 0;

            for (var element : recipe.getAsJsonArray("results")) {

                JsonObject result = element.getAsJsonObject();

                if (!result.has("output")
                        || !result.get("output").isJsonObject()) {
                    continue;
                }

                JsonObject output = result.getAsJsonObject("output");

                if (!output.has("id")
                        || !seedId.equals(output.get("id").getAsString())) {
                    continue;
                }

                seedOutputs++;

                assertTrue(
                        result.has("chance"),
                        "Missing seed drop chance in " + filename
                );

                float chance = result.get("chance").getAsFloat();

                if (vanilla) {
                    fail("Vanilla soil must not drop extra seeds: " + filename);
                }

                boolean is10Percent = Math.abs(chance - 0.10f) < 0.0001f;
                boolean is20Percent = Math.abs(chance - 0.20f) < 0.0001f;

                assertTrue(
                        is10Percent || is20Percent,
                        "Unexpected seed drop chance in " + filename
                                + ": " + chance
                );

                if (is20Percent) {
                    matchingTierRecipes.merge(seedId, 1, Integer::sum);
                }
            }

            assertEquals(
                    vanilla ? 0 : 1,
                    seedOutputs,
                    "Incorrect number of extra seed outputs in " + filename
            );
        }

        // Each crop must have exactly one recipe with 20% seed chance.
        matchingTierRecipes.forEach((seedId, count) ->
                assertEquals(
                        1,
                        count,
                        "Expected exactly one matching-tier seed bonus for "
                                + seedId
                )
        );
    }

    @Test
    void allRecipeGrowthTimesShouldMatchCalculator()
            throws IOException {

        List<Path> allRecipes;

        try (Stream<Path> files = Files.list(RECIPE_DIRECTORY)) {
            allRecipes = files
                    .filter(path -> path.toString().endsWith(".json"))
                    .toList();
        }

        List<Path> vanillaRecipes = allRecipes.stream()
                .filter(path -> path.getFileName()
                        .toString().endsWith("_from_vanilla.json"))
                .toList();

        assertFalse(vanillaRecipes.isEmpty(), "No vanilla recipes found.");

        int validatedRecipes = 0;

        for (Path vanillaPath : vanillaRecipes) {

            String vanillaFilename = vanillaPath.getFileName().toString();

            String prefix = vanillaFilename.substring(
                    0,
                    vanillaFilename.length() - "vanilla.json".length()
            );

            JsonObject vanillaRecipe = readRecipe(vanillaFilename);

            int vanillaTime = vanillaRecipe.get("time").getAsInt();

            String seedId = vanillaRecipe
                    .getAsJsonObject("input")
                    .get("item")
                    .getAsString();

            // Infer the seed tier from the vanilla soil growth time.
            int seedTier = -1;

            for (int tier = 1; tier <= 5; tier++) {
                if (ClocheCalculator.calculateTime(tier, 0) == vanillaTime) {
                    seedTier = tier;
                    break;
                }
            }

            assertTrue(
                    seedTier >= 1,
                    "Invalid vanilla growth time in " + vanillaFilename
            );

            // Validate all seven farmland recipes for this crop.
            for (var farmland : INFERIUM_FARMLAND_TIERS.entrySet()) {

                String filename = prefix + farmland.getKey() + ".json";

                JsonObject recipe = readRecipe(filename);

                String actualSeedId = recipe
                        .getAsJsonObject("input")
                        .get("item")
                        .getAsString();

                assertEquals(
                        seedId,
                        actualSeedId,
                        "Incorrect seed in " + filename
                );

                int expectedTime = ClocheCalculator.calculateTime(
                        seedTier,
                        farmland.getValue()
                );

                int actualTime = recipe.get("time").getAsInt();

                assertEquals(
                        expectedTime,
                        actualTime,
                        "Incorrect growth time in " + filename
                );

                validatedRecipes++;
            }
        }

        assertEquals(
                allRecipes.size(),
                validatedRecipes,
                "Not all generated recipes were validated."
        );
    }

    @Test
    void matchingFarmlandSeedBonusShouldMatchCropTier()
            throws IOException {

        List<Path> vanillaRecipes;

        try (Stream<Path> files = Files.list(RECIPE_DIRECTORY)) {
            vanillaRecipes = files
                    .filter(path -> path.getFileName()
                            .toString().endsWith("_from_vanilla.json"))
                    .toList();
        }

        assertFalse(vanillaRecipes.isEmpty(), "No vanilla recipes found.");

        for (Path vanillaPath : vanillaRecipes) {

            String vanillaFilename = vanillaPath.getFileName().toString();
            JsonObject vanillaRecipe = readRecipe(vanillaFilename);

            int vanillaTime = vanillaRecipe.get("time").getAsInt();

            // Infer crop tier from its vanilla growth time.
            int seedTier = -1;

            for (int tier = 1; tier <= 5; tier++) {
                if (ClocheCalculator.calculateTime(tier, 0) == vanillaTime) {
                    seedTier = tier;
                    break;
                }
            }

            assertTrue(
                    seedTier >= 1,
                    "Unable to infer crop tier in " + vanillaFilename
            );

            // Identify the farmland matching the inferred crop tier.
            String matchingFarmland = null;

            for (var entry : INFERIUM_FARMLAND_TIERS.entrySet()) {
                if (entry.getValue() == seedTier) {
                    matchingFarmland = entry.getKey();
                    break;
                }
            }

            assertNotNull(
                    matchingFarmland,
                    "No matching farmland for tier " + seedTier
            );

            String filename = vanillaFilename.replace(
                    "_from_vanilla.json",
                    "_from_" + matchingFarmland + ".json"
            );

            JsonObject recipe = readRecipe(filename);

            String seedId = vanillaRecipe.getAsJsonObject("input")
                    .get("item").getAsString();

            assertEquals(
                    seedId,
                    recipe.getAsJsonObject("input").get("item").getAsString(),
                    "Incorrect seed in " + filename
            );

            int seedOutputs = 0;

            for (var element : recipe.getAsJsonArray("results")) {

                JsonObject result = element.getAsJsonObject();

                if (!result.has("output")
                        || !result.get("output").isJsonObject()) {
                    continue;
                }

                JsonObject output = result.getAsJsonObject("output");

                if (!output.has("id")
                        || !seedId.equals(output.get("id").getAsString())) {
                    continue;
                }

                seedOutputs++;

                assertTrue(
                        result.has("chance"),
                        "Missing seed chance in " + filename
                );

                assertEquals(
                        ClocheCalculator.calculateSeedDropChance(
                                seedTier, seedTier
                        ),
                        result.get("chance").getAsFloat(),
                        0.0001f,
                        "Incorrect matching-tier seed bonus in " + filename
                );
            }

            assertEquals(
                    1,
                    seedOutputs,
                    "Expected one extra seed output in " + filename
            );
        }
    }

    @Test
    void resourceCropRecipesShouldProduceExactlyOneEssence()
            throws IOException {

        List<Path> recipes;

        try (Stream<Path> files = Files.list(RECIPE_DIRECTORY)) {
            recipes = files
                    .filter(path -> path.toString().endsWith(".json"))
                    .toList();
        }

        assertFalse(recipes.isEmpty(), "No recipes found.");

        int validatedRecipes = 0;

        for (Path path : recipes) {

            String filename = path.getFileName().toString();
            JsonObject recipe = readRecipe(filename);

            String seedId = recipe.getAsJsonObject("input")
                    .get("item").getAsString();

            // Inferium has its own variable output rules.
            if ("mysticalagriculture:inferium_seeds".equals(seedId)) {
                continue;
            }

            var results = recipe.getAsJsonArray("results");

            assertTrue(
                    results.size() > 0,
                    "Missing recipe outputs in " + filename
            );

            // The first output must be the guaranteed essence.
            JsonObject mainOutput = results.get(0).getAsJsonObject();

            assertTrue(
                    mainOutput.has("id"),
                    "Missing guaranteed essence in " + filename
            );

            String essenceId = mainOutput.get("id").getAsString();

            assertTrue(
                    essenceId.startsWith("mysticalagriculture:")
                            && essenceId.endsWith("_essence"),
                    "Unexpected main output in " + filename
                            + ": " + essenceId
            );

            int guaranteedCount = mainOutput.has("count")
                    ? mainOutput.get("count").getAsInt()
                    : 1;

            assertEquals(
                    1,
                    guaranteedCount,
                    "Incorrect guaranteed essence count in " + filename
            );

            assertFalse(
                    mainOutput.has("chance"),
                    "Main essence output must be guaranteed in " + filename
            );

            // No additional guaranteed or probabilistic output
            // should grant more of the same resource essence.
            for (int i = 1; i < results.size(); i++) {

                JsonObject result = results.get(i).getAsJsonObject();

                if (result.has("id")) {
                    assertNotEquals(
                            essenceId,
                            result.get("id").getAsString(),
                            "Duplicate guaranteed essence in " + filename
                    );
                }

                if (result.has("output")
                        && result.get("output").isJsonObject()) {

                    JsonObject extra = result.getAsJsonObject("output");

                    if (extra.has("id")) {
                        assertNotEquals(
                                essenceId,
                                extra.get("id").getAsString(),
                                "Unexpected bonus essence in " + filename
                        );
                    }
                }
            }

            validatedRecipes++;
        }

        assertTrue(
                validatedRecipes > 0,
                "No resource crop recipes were validated."
        );
    }

    @Test
    void allRecipesShouldUseCorrectFarmlandItems()
            throws IOException {

        Map<String, String> expectedSoils = Map.of(
                "vanilla", "minecraft:dirt",
                "inferium", "mysticalagriculture:inferium_farmland",
                "prudentium", "mysticalagriculture:prudentium_farmland",
                "tertium", "mysticalagriculture:tertium_farmland",
                "imperium", "mysticalagriculture:imperium_farmland",
                "supremium", "mysticalagriculture:supremium_farmland",
                "insanium", "mysticalagradditions:insanium_farmland"
        );

        List<Path> recipes;

        try (Stream<Path> files = Files.list(RECIPE_DIRECTORY)) {
            recipes = files
                    .filter(path -> path.toString().endsWith(".json"))
                    .toList();
        }

        assertFalse(recipes.isEmpty(), "No recipes found.");

        for (Path path : recipes) {

            String filename = path.getFileName().toString();

            String farmlandName = null;

            for (String name : expectedSoils.keySet()) {
                if (filename.endsWith("_from_" + name + ".json")) {
                    farmlandName = name;
                    break;
                }
            }

            assertNotNull(
                    farmlandName,
                    "Unknown farmland tier in " + filename
            );

            JsonObject recipe = readRecipe(filename);

            String actualSoil = recipe
                    .getAsJsonObject("soil")
                    .get("item")
                    .getAsString();

            assertEquals(
                    expectedSoils.get(farmlandName),
                    actualSoil,
                    "Incorrect farmland item in " + filename
            );
        }
    }

    @Test
    void allRecipesShouldMatchCropIdentifiers()
            throws IOException {

        List<Path> recipes;

        try (Stream<Path> files = Files.list(RECIPE_DIRECTORY)) {
            recipes = files
                    .filter(path -> path.toString().endsWith(".json"))
                    .toList();
        }

        assertFalse(recipes.isEmpty(), "No recipes found.");

        for (Path path : recipes) {

            String filename = path.getFileName().toString();

            assertTrue(
                    filename.startsWith("cloche_"),
                    "Unexpected recipe filename: " + filename
            );

            int separatorIndex = filename.lastIndexOf("_from_");

            assertTrue(
                    separatorIndex > "cloche_".length(),
                    "Cannot identify crop name in " + filename
            );

            String cropName = filename.substring(
                    "cloche_".length(),
                    separatorIndex
            );

            String expectedSeed =
                    "mysticalagriculture:" + cropName + "_seeds";

            String expectedCrop =
                    "mysticalagriculture:" + cropName + "_crop";

            String expectedEssence =
                    "mysticalagriculture:" + cropName + "_essence";

            JsonObject recipe = readRecipe(filename);

            String actualSeed = recipe
                    .getAsJsonObject("input")
                    .get("item")
                    .getAsString();

            JsonObject render = recipe.getAsJsonObject("render");

            String actualCrop = render
                    .get("block")
                    .getAsString();

            String actualEssence = recipe
                    .getAsJsonArray("results")
                    .get(0)
                    .getAsJsonObject()
                    .get("id")
                    .getAsString();

            assertAll(filename,

                    () -> assertEquals(
                            expectedSeed,
                            actualSeed,
                            "Incorrect seed item"
                    ),

                    () -> assertEquals(
                            "immersiveengineering:crop",
                            render.get("type").getAsString(),
                            "Incorrect render type"
                    ),

                    () -> assertEquals(
                            expectedCrop,
                            actualCrop,
                            "Incorrect crop block"
                    ),

                    () -> assertEquals(
                            expectedEssence,
                            actualEssence,
                            "Incorrect primary essence output"
                    )
            );
        }
    }
}
