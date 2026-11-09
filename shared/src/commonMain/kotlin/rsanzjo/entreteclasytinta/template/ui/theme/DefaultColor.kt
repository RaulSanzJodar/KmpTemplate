package rsanzjo.entreteclasytinta.template.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val primaryLight = Color(0xFF8F4C38)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFFFFDBD1)
val onPrimaryContainerLight = Color(0xFF723523)
val secondaryLight = Color(0xFF77574E)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFFFDBD1)
val onSecondaryContainerLight = Color(0xFF5D4037)
val tertiaryLight = Color(0xFF6C5D2F)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFF5E1A7)
val onTertiaryContainerLight = Color(0xFF534619)
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF93000A)
val backgroundLight = Color(0xFFFAF8F7)
val onBackgroundLight = Color(0xFF1F1B1A)
val surfaceLight = Color(0xFFFAF8F7)
val onSurfaceLight = Color(0xFF1F1B1A)
val surfaceVariantLight = Color(0xFFEFE6E3)
val onSurfaceVariantLight = Color(0xFF4F4542)
val outlineLight = Color(0xFF80756F)
val outlineVariantLight = Color(0xFFD3C8C4)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF34302F)
val inverseOnSurfaceLight = Color(0xFFF8F0EE)
val inversePrimaryLight = Color(0xFFFFB5A0)
val surfaceDimLight = Color(0xFFE2DBD8)
val surfaceBrightLight = Color(0xFFFAF8F7)
val surfaceContainerLowestLight = Color(0xFFFFFFFF)
val surfaceContainerLowLight = Color(0xFFF6F2F0)
val surfaceContainerLight = Color(0xFFF1ECEA)
val surfaceContainerHighLight = Color(0xFFEBE6E3)
val surfaceContainerHighestLight = Color(0xFFE5E0DD)

val primaryDark = Color(0xFFFFB5A0)
val onPrimaryDark = Color(0xFF561F0F)
val primaryContainerDark = Color(0xFF723523)
val onPrimaryContainerDark = Color(0xFFFFDBD1)
val secondaryDark = Color(0xFFE7BDB2)
val onSecondaryDark = Color(0xFF442A22)
val secondaryContainerDark = Color(0xFF5D4037)
val onSecondaryContainerDark = Color(0xFFFFDBD1)
val tertiaryDark = Color(0xFFD8C58D)
val onTertiaryDark = Color(0xFF3B2F05)
val tertiaryContainerDark = Color(0xFF534619)
val onTertiaryContainerDark = Color(0xFFF5E1A7)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val backgroundDark = Color(0xFF151211)
val onBackgroundDark = Color(0xFFEAE1DF)
val surfaceDark = Color(0xFF151211)
val onSurfaceDark = Color(0xFFEAE1DF)
val surfaceVariantDark = Color(0xFF4F4542)
val onSurfaceVariantDark = Color(0xFFD3C8C4)
val outlineDark = Color(0xFF9B8F8B)
val outlineVariantDark = Color(0xFF4F4542)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFEAE1DF)
val inverseOnSurfaceDark = Color(0xFF34302F)
val inversePrimaryDark = Color(0xFF8F4C38)
val surfaceDimDark = Color(0xFF151211)
val surfaceBrightDark = Color(0xFF3B3736)
val surfaceContainerLowestDark = Color(0xFF100D0C)
val surfaceContainerLowDark = Color(0xFF1D1A19)
val surfaceContainerDark = Color(0xFF211E1D)
val surfaceContainerHighDark = Color(0xFF2C2827)
val surfaceContainerHighestDark = Color(0xFF373332)

val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

/** Colores de una tarjeta de equipo: barra lateral, fondo, acento y texto sobre el fondo. */
@Immutable
data class EquipmentCardPalette(
    val sidebarBg: Color,
    val sidebarText: Color,
    val sidebarValue: Color,
    val mainBg: Color,
    val accent: Color,
    val content: Color,
    val notProficient: Color
)

/** Color de cada escuela de magia, ya ajustado para leerse sobre el fondo del tema. */
@Immutable
data class SpellSchoolPalette(
    val evocation: Color,
    val abjuration: Color,
    val conjuration: Color,
    val transmutation: Color,
    val necromancy: Color,
    val illusion: Color,
    val divination: Color,
    val enchantment: Color
)

@Immutable
data class DefaultColors(
    val statHitPoints: Color,
    val statInitiative: Color,
    val statSpeed: Color,
    val statArmorClass: Color,
    val statProficiencyBonus: Color,
    val cardBackground: Color,
    val cardBorder: Color,
    val favoriteGold: Color,
    val favoriteInactive: Color,
    val shimmerBase: Color,
    val shimmerHighlight: Color,
    /** Color de una moneda cuyo API no trae color. */
    val coinFallback: Color,

    // Equipment Palette
    val equipmentWeapon: EquipmentCardPalette,
    val equipmentArmor: EquipmentCardPalette,
    val equipmentGeneral: EquipmentCardPalette,
    /** Bonificador de daño de un rasgo activo, sobre la tarjeta de arma. */
    val equipmentTraitBonus: Color,
    /** Icono sobre el acento de sección (botones flotantes de equipo). */
    val equipmentOnAccent: Color,

    val spellSchools: SpellSchoolPalette,
    val spellSchoolFallback: Color,

    // Section Colors
    val equipmentArsenalAccent: Color,
    val equipmentArsenalContainer: Color,
    val equipmentDefenseAccent: Color,
    val equipmentDefenseContainer: Color,
    val equipmentTreasureAccent: Color,
    val equipmentTreasureContainer: Color,
    val equipmentLogisticsAccent: Color,
    val equipmentLogisticsContainer: Color,

    // Background Tab Colors
    val backgroundHeaderIcon: Color,
    val backgroundAlignmentTagBg: Color,
    val backgroundAlignmentTagContent: Color,
    val backgroundDeityTagBg: Color,
    val backgroundDeityTagContent: Color,
    val backgroundPersonalityAccent: Color,
    val backgroundIdealsAccent: Color,
    val backgroundBondsAccent: Color,
    val backgroundFlawsAccent: Color
)

val hitPointsLight = Color(0xFFC75B5B)
val initiativeLight = Color(0xFFA88A00)
val speedLight = Color(0xFF2E9C6A)
val armorClassLight = Color(0xFF2F86B5)
val proficiencyBonusLight = Color(0xFF8E63BD)
val cardBackgroundLight = Color(0xFFFFFFFF)

val hitPointsDark = Color(0xFFC97A7A)
val initiativeDark = Color(0xFFC9B86E)
val speedDark = Color(0xFF7FB59A)
val armorClassDark = Color(0xFF7AA7C2)
val proficiencyBonusDark = Color(0xFF9C82B8)
val cardBackgroundDark = Color(0xFF211E1D)

val LightDefaultColors = DefaultColors(
    statHitPoints = hitPointsLight,
    statInitiative = initiativeLight,
    statSpeed = speedLight,
    statArmorClass = armorClassLight,
    statProficiencyBonus = proficiencyBonusLight,
    cardBackground = cardBackgroundLight,
    
    cardBorder = Color(0x1F000000),
    favoriteGold = Color(0xFFFFB300),
    favoriteInactive = Color(0x809E9E9E),
    shimmerBase = Color(0xFFB8B5B5),
    shimmerHighlight = Color(0xFF8F8B8B),
    coinFallback = Color(0xFFCCCCCC),

    equipmentWeapon = EquipmentCardPalette(
        sidebarBg = Color(0xFF2D323E), sidebarText = Color(0xFFB0B4BC), sidebarValue = Color(0xFFFFFFFF),
        mainBg = Color(0xFF4B515D), accent = Color(0xFFFF8A80), content = Color(0xFFFFFFFF), notProficient = Color(0xFFEF9A9A)
    ),
    equipmentArmor = EquipmentCardPalette(
        sidebarBg = Color(0xFFB3BAE0), sidebarText = Color(0xFF303F9F), sidebarValue = Color(0xFF1A237E),
        mainBg = Color(0xFFDDE1F3), accent = Color(0xFF303F9F), content = Color(0xFF263238), notProficient = Color(0xFFC62828)
    ),
    equipmentGeneral = EquipmentCardPalette(
        sidebarBg = Color(0xFFA0D3CE), sidebarText = Color(0xFF005B50), sidebarValue = Color(0xFF00382F),
        mainBg = Color(0xFFD3ECEA), accent = Color(0xFF00695C), content = Color(0xFF263238), notProficient = Color(0xFFC62828)
    ),
    equipmentTraitBonus = Color(0xFFFFB74D),
    equipmentOnAccent = Color(0xFFFFFFFF),

    spellSchools = SpellSchoolPalette(
        evocation = Color(0xFF942522), abjuration = Color(0xFF135894), conjuration = Color(0xFF5C176E),
        transmutation = Color(0xFFA55C00), necromancy = Color(0xFF36474F), illusion = Color(0xFF3D2273),
        divination = Color(0xFF006F7D), enchantment = Color(0xFF8C113E)
    ),
    spellSchoolFallback = Color(0xFF6750A4),

    // Section Colors
    equipmentArsenalAccent = Color(0xFFD32F2F),
    equipmentArsenalContainer = Color(0xFFFEEBEE),
    equipmentDefenseAccent = Color(0xFF3F51B5),
    equipmentDefenseContainer = Color(0xFFE8EAF6),
    equipmentTreasureAccent = Color(0xFFE65100),
    equipmentTreasureContainer = Color(0xFFFFF8E1),
    equipmentLogisticsAccent = Color(0xFF2E7D32),
    equipmentLogisticsContainer = Color(0xFFE8F5E9),

    // Background Tab Colors
    backgroundHeaderIcon = Color(0xFFB8631F),
    backgroundAlignmentTagBg = Color(0xFFE8EAF6),
    backgroundAlignmentTagContent = Color(0xFF3F51B5),
    backgroundDeityTagBg = Color(0xFFFFF8E1),
    backgroundDeityTagContent = Color(0xFFA85400),
    backgroundPersonalityAccent = Color(0xFF7E57C2),
    backgroundIdealsAccent = Color(0xFFA67C00),
    backgroundBondsAccent = Color(0xFF1E88E5),
    backgroundFlawsAccent = Color(0xFFE53935)
)

val DarkDefaultColors = DefaultColors(
    statHitPoints = hitPointsDark,
    statInitiative = initiativeDark,
    statSpeed = speedDark,
    statArmorClass = armorClassDark,
    statProficiencyBonus = proficiencyBonusDark,
    cardBackground = cardBackgroundDark,

    cardBorder = Color(0x29FFFFFF),
    favoriteGold = Color(0xFFFFC107),
    favoriteInactive = Color(0x809E9E9E),
    shimmerBase = Color(0xFF3B3736),
    shimmerHighlight = Color(0xFF2C2827),
    coinFallback = Color(0xFF8A8280),

    equipmentWeapon = EquipmentCardPalette(
        sidebarBg = Color(0xFF181B22), sidebarText = Color(0xFFB4B9C3), sidebarValue = Color(0xFFFFFFFF),
        mainBg = Color(0xFF464D5C), accent = Color(0xFFFF9E95), content = Color(0xFFFFFFFF), notProficient = Color(0xFFFFAB9F)
    ),
    equipmentArmor = EquipmentCardPalette(
        sidebarBg = Color(0xFF181C30), sidebarText = Color(0xFFB3BDF2), sidebarValue = Color(0xFFE6E9FF),
        mainBg = Color(0xFF38406B), accent = Color(0xFFB0B8F8), content = Color(0xFFF0F1FA), notProficient = Color(0xFFFFAB9F)
    ),
    equipmentGeneral = EquipmentCardPalette(
        sidebarBg = Color(0xFF112826), sidebarText = Color(0xFF8FD6CF), sidebarValue = Color(0xFFD2F7F2),
        mainBg = Color(0xFF2A524F), accent = Color(0xFF6BCBC1), content = Color(0xFFEAF7F6), notProficient = Color(0xFFFFAB9F)
    ),
    equipmentTraitBonus = Color(0xFFFFB74D),
    equipmentOnAccent = Color(0xFF1F1B1A),

    spellSchools = SpellSchoolPalette(
        evocation = Color(0xFFEF8885), abjuration = Color(0xFF78B7EF), conjuration = Color(0xFFBB7BCC),
        transmutation = Color(0xFFFFBB66), necromancy = Color(0xFF98A8AF), illusion = Color(0xFF9E85D0),
        divination = Color(0xFF66CDD9), enchantment = Color(0xFFE7769F)
    ),
    spellSchoolFallback = Color(0xFFD0BCFF),

    // Section Colors
    equipmentArsenalAccent = Color(0xFFEF5350),
    equipmentArsenalContainer = Color(0xFF3B2422),
    equipmentDefenseAccent = Color(0xFF7986CB),
    equipmentDefenseContainer = Color(0xFF242938),
    equipmentTreasureAccent = Color(0xFFFF8F00),
    equipmentTreasureContainer = Color(0xFF3A2D1A),
    equipmentLogisticsAccent = Color(0xFF66BB6A),
    equipmentLogisticsContainer = Color(0xFF22331F),

    // Background Tab Colors (Keeping mostly same as light for now per style, but containers could be darkened)
    backgroundHeaderIcon = Color(0xFFD4813B),
    backgroundAlignmentTagBg = Color(0xFF3F51B5).copy(alpha = 0.2f), // Darker bg for tag
    backgroundAlignmentTagContent = Color(0xFF9FA8DA), // Lighter text
    backgroundDeityTagBg = Color(0xFFFF8F00).copy(alpha = 0.2f),
    backgroundDeityTagContent = Color(0xFFFFCC80),
    backgroundPersonalityAccent = Color(0xFFB39DDB),
    backgroundIdealsAccent = Color(0xFFFBC02D),
    backgroundBondsAccent = Color(0xFF42A5F5),
    backgroundFlawsAccent = Color(0xFFEF5350)
)

val LocalDefaultColors = staticCompositionLocalOf { LightDefaultColors }