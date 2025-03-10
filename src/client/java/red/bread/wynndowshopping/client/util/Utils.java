package red.bread.wynndowshopping.client.util;


import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Utils {
    public static Map<String,String> legacyNameMap = Map.<String, String>ofEntries(Map.entry("1:3","diorite"),Map.entry("1:5","andesite"),Map.entry("1:6","polished_andesite"),Map.entry("3:1","coarse_dirt"),Map.entry("6:1","spruce_sapling"),Map.entry("6:2","birch_sapling"),Map.entry("6:3","jungle_sapling"),Map.entry("6:4","acacia_sapling"),Map.entry("6:5","dark_oak_sapling"),Map.entry("12:0","sand"),Map.entry("13:0","gravel"),Map.entry("14:0","gold_ore"),Map.entry("15:0","iron_ore"),Map.entry("16:0","coal_ore"),Map.entry("19:0","sponge"),Map.entry("19:1","wet_sponge"),Map.entry("21:0","lapis_ore"),Map.entry("22:0","lapis_block"),Map.entry("25:0","note_block"),Map.entry("29:0","sticky_piston"),Map.entry("30:0","cobweb"),Map.entry("31:1","tall_grass"),Map.entry("31:2","fern"),Map.entry("32:0","dead_bush"),Map.entry("35:0","white_wool"),Map.entry("35:3","light_blue_wool"),Map.entry("35:4","yellow_wool"),Map.entry("35:6","pink_wool"),Map.entry("35:13","green_wool"),Map.entry("35:14","red_wool"),Map.entry("37:0","dandelion"),Map.entry("38:0","poppy"),Map.entry("38:1","blue_orchid"),Map.entry("38:2","allium"),Map.entry("38:3","azure_bluet"),Map.entry("38:4","red_tulip"),Map.entry("38:7","pink_tulip"),Map.entry("38:8","oxeye_daisy"),Map.entry("39:0","brown_mushroom"),Map.entry("40:0","red_mushroom"),Map.entry("41:0","gold_block"),Map.entry("42:0","iron_block"),Map.entry("46:0","tnt"),Map.entry("50:0","torch"),Map.entry("67:0","cobblestone_stairs"),Map.entry("69:0","lever"),Map.entry("70:0","stone_pressure_plate"),Map.entry("73:0","redstone_ore"),Map.entry("76:0","redstone_torch"),Map.entry("77:0","stone_button"),Map.entry("79:0","ice"),Map.entry("80:0","snow_block"),Map.entry("81:0","cactus"),Map.entry("86:0","pumpkin"),Map.entry("86","carved_pumpkin"),Map.entry("87:0","netherrack"),Map.entry("95:3","light_blue_stained_glass"),Map.entry("95:14","red_stained_glass"),Map.entry("95:15","black_stained_glass"),Map.entry("97:4","infested_cracked_stone_bricks"),Map.entry("98:1","mossy_stone_bricks"),Map.entry("98:3","chiseled_stone_bricks"),Map.entry("101:0","iron_bars"),Map.entry("106:0","vine"),Map.entry("111:0","lily_pad"),Map.entry("121:0","end_stone"),Map.entry("122:0","dragon_egg"),Map.entry("123:0","redstone_lamp"),Map.entry("129:0","emerald_ore"),Map.entry("138:0","beacon"),Map.entry("145:0","anvil"),Map.entry("148:0","heavy_weighted_pressure_plate"),Map.entry("152:0","redstone_block"),Map.entry("153:0","nether_quartz_ore"),Map.entry("154:0","hopper"),Map.entry("155:2","quartz_pillar"),Map.entry("160:14","black_terracotta"),Map.entry("160:15","black_stained_glass_pane"),Map.entry("165:0","slime_block"),Map.entry("168:0","prismarine"),Map.entry("168:1","prismarine_bricks"),Map.entry("168:2","dark_prismarine"),Map.entry("169:0","sea_lantern"),Map.entry("170:0","hay_block"),Map.entry("171:0","white_carpet"),Map.entry("171:7","gray_carpet"),Map.entry("171:14","red_carpet"),Map.entry("173:0","coal_block"),Map.entry("174:0","packed_ice"),Map.entry("175:0","sunflower"),Map.entry("175:1","lilac"),Map.entry("175:3","large_fern"),Map.entry("175:4","rose_bush"),Map.entry("179:1","red_sandstone"),Map.entry("198:0","end_rod"),Map.entry("199:0","chorus_plant"),Map.entry("200:0","chorus_flower"),Map.entry("213:0","magma_block"),Map.entry("235:0","white_glazed_terracotta"),Map.entry("238:0","light_blue_glazed_terracotta"),Map.entry("239:0","yellow_glazed_terracotta"),Map.entry("245:0","purple_glazed_terracotta"),Map.entry("246:0","blue_glazed_terracotta"),Map.entry("249:0","red_glazed_terracotta"),Map.entry("250:0","black_glazed_terracotta"),Map.entry("251:15","black_concrete"),Map.entry("252:15","black_concrete_powder"),Map.entry("257:0","iron_pickaxe"),Map.entry("259:0","flint_and_steel"),Map.entry("259:1","flint_and_steel"),Map.entry("259:18","flint_and_steel"),Map.entry("259:21","flint_and_steel"),Map.entry("259:34","flint_and_steel"),Map.entry("260:0","apple"),Map.entry("263:0","coal"),Map.entry("263:1","charcoal"),Map.entry("264:0","diamond"),Map.entry("265:0","iron_ingot"),Map.entry("266:0","gold_ingot"),Map.entry("271:0","wooden_axe"),Map.entry("273:7","stone_shovel"),Map.entry("273:36","stone_shovel"),Map.entry("278:26","diamond_pickaxe"),Map.entry("280:0","stick"),Map.entry("281:0","bowl"),Map.entry("282:0","mushroom_stew"),Map.entry("287:0","string"),Map.entry("288:0","feather"),Map.entry("289:0","gunpowder"),Map.entry("295:0","wheat_seeds"),Map.entry("296:0","wheat"),Map.entry("302:0","chainmail_helmet"),Map.entry("303:0","chainmail_chestplate"),Map.entry("314:0","golden_helmet"),Map.entry("318:0","flint"),Map.entry("319:0","porkchop"),Map.entry("320:0","cooked_porkchop"),Map.entry("321:0","painting"),Map.entry("323:0","oak_sign"),Map.entry("325:0","bucket"),Map.entry("326:0","water_bucket"),Map.entry("331:0","redstone"),Map.entry("332:0","snowball"),Map.entry("334:0","leather"),Map.entry("336:0","brick"),Map.entry("337:0","clay_ball"),Map.entry("338:0","sugar_cane"),Map.entry("339:0","paper"),Map.entry("340:0","book"),Map.entry("341:0","slime_ball"),Map.entry("344:0","egg"),Map.entry("348:0","glowstone_dust"),Map.entry("349:0","cod"),Map.entry("349:1","salmon"),Map.entry("349:2","tropical_fish"),Map.entry("350:0","cooked_cod"),Map.entry("350:1","cooked_salmon"),Map.entry("351:0","ink_sac"),Map.entry("351:3","cocoa_beans"),Map.entry("351:4","lapis_lazuli"),Map.entry("351:5","purple_dye"),Map.entry("351:12","light_blue_dye"),Map.entry("351:15","bone_meal"),Map.entry("352:0","bone"),Map.entry("353:0","sugar"),Map.entry("356:0","repeater"),Map.entry("357:0","cookie"),Map.entry("360:0","melon_slice"),Map.entry("361:0","pumpkin_seeds"),Map.entry("362:0","melon_seeds"),Map.entry("363:0","beef"),Map.entry("367:0","rotten_flesh"),Map.entry("368:0","ender_pearl"),Map.entry("369:0","blaze_rod"),Map.entry("370:0","ghast_tear"),Map.entry("371:0","gold_nugget"),Map.entry("372:0","nether_wart"),Map.entry("373:0","potion"),Map.entry("374:0","glass_bottle"),Map.entry("375:0","spider_eye"),Map.entry("376:0","fermented_spider_eye"),Map.entry("377:0","blaze_powder"),Map.entry("378:0","magma_cream"),Map.entry("379:0","brewing_stand"),Map.entry("381:0","ender_eye"),Map.entry("382:0","glistering_melon_slice"),Map.entry("383:0","wolf_spawn_egg"),Map.entry("383:5","wither_skeleton_spawn_egg"),Map.entry("383:55","slime_spawn_egg"),Map.entry("383:58","enderman_spawn_egg"),Map.entry("383:120","villager_spawn_egg"),Map.entry("385:0","fire_charge"),Map.entry("390:0","flower_pot"),Map.entry("392:0","potato"),Map.entry("396:0","golden_carrot"),Map.entry("397:2","zombie_head"),Map.entry("400:0","pumpkin_pie"),Map.entry("402:0","firework_star"),Map.entry("405:0","nether_brick"),Map.entry("406:0","quartz"),Map.entry("408:0","hopper_minecart"),Map.entry("409:0","prismarine_shard"),Map.entry("410:0","prismarine_crystals"),Map.entry("411:0","rabbit"),Map.entry("412:0","cooked_rabbit"),Map.entry("419:0","diamond_horse_armor"),Map.entry("423:0","mutton"),Map.entry("424:0","cooked_mutton"),Map.entry("432:0","chorus_fruit"),Map.entry("433:0","popped_chorus_fruit"),Map.entry("434:0","beetroot"),Map.entry("435:0","beetroot_seeds"),Map.entry("437:0","dragon_breath"),Map.entry("441:0","lingering_potion"),Map.entry("449:0","totem_of_undying"),Map.entry("452:0","iron_nugget"));

    public static Color parseRGB(String rgbString) {
        String[] rgbValues = rgbString.replaceAll("\\s", ",").replaceAll("[^0-9,]", "").split(",");

        return new Color(Integer.parseInt(rgbValues[0]),
                Integer.parseInt(rgbValues[1]),
                Integer.parseInt(rgbValues[2]));
    }

    public static String toUpperCamelCaseWithSpaces(String camelCase) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < camelCase.length(); i++) {
            char currentChar = camelCase.charAt(i);

            if (i == 0 || Character.isUpperCase(currentChar)) {
                if (i > 0) {
                    result.append(" ");
                }
                result.append(Character.toUpperCase(currentChar));
            } else {
                result.append(currentChar);
            }
        }

        return result.toString();
    }

    public static String snakeToUpperCamelCaseWithSpaces(String snakeCase) {
        String[] words = snakeCase.split("_");

        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase());
                result.append(" ");
            }
        }

        if (!result.isEmpty()) {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }

    public static List<String> splitStringByLength(String input, int maxLength) {
        List<String> result = new ArrayList<>();
        int start = 0;
        while (start < input.length()) {
            int end = Math.min(start + maxLength, input.length());
            if (end < input.length() && input.charAt(end) != ' ') {
                int lastSpaceIndex = input.lastIndexOf(' ', end);
                if (lastSpaceIndex > start) {
                    end = lastSpaceIndex;
                }
            }
            result.add(input.substring(start, end));
            start = end;
            while (start < input.length() && input.charAt(start) == ' ') {
                start++;
            }
        }
        return result;
    }

    public static String getProfessionIcon(String profession) {
        Map<String, String> professionIcons = new HashMap<>();
        professionIcons.put("woodcutting", "Ⓒ");
        professionIcons.put("mining", "Ⓑ");
        professionIcons.put("fishing", "Ⓚ");
        professionIcons.put("farming", "Ⓙ");
        professionIcons.put("alchemism", "Ⓛ");
        professionIcons.put("armouring", "Ⓗ");
        professionIcons.put("cooking", "Ⓐ");
        professionIcons.put("jeweling", "Ⓓ");
        professionIcons.put("scribing", "Ⓔ");
        professionIcons.put("tailoring", "Ⓕ");
        professionIcons.put("weaponsmithing", "Ⓖ");
        professionIcons.put("woodworking", "Ⓘ");

        return professionIcons.getOrDefault(profession, "?");
    }

    public static double eval(final String str) {
        // Taken from https://stackoverflow.com/questions/3422673/how-to-evaluate-a-math-expression-given-in-string-form/
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')')) throw new RuntimeException("Missing ')' after argument to " + func);
                    } else {
                        x = parseFactor();
                    }
                    x = switch (func) {
                        case "sqrt" -> Math.sqrt(x);
                        case "sin" -> Math.sin(Math.toRadians(x));
                        case "cos" -> Math.cos(Math.toRadians(x));
                        case "tan" -> Math.tan(Math.toRadians(x));
                        default -> throw new RuntimeException("Unknown function: " + func);
                    };
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}
