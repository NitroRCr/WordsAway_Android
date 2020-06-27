package com.krytro.wordsaway;

import android.os.Build;
import java.util.*;

public class WordsAway {
    Map<String, Map<String, List<String>>> styles = new HashMap<String, Map<String, List<String>>>(){{
        put("letters", new HashMap<String, List<String>>(){{
            put("normal", stringListed("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ",
                    false));
            put("bold", stringListed("𝐚𝐛𝐜𝐝𝐞𝐟𝐠𝐡𝐢𝐣𝐤𝐥𝐦𝐧𝐨𝐩𝐪𝐫𝐬𝐭𝐮𝐯𝐰𝐱𝐲𝐳𝐀𝐁𝐂𝐃𝐄𝐅𝐆𝐇𝐈𝐉𝐊𝐋𝐌𝐍𝐎𝐏𝐐𝐑𝐒𝐓𝐔𝐕𝐖𝐗𝐘𝐙",
                    false));
            put("italic", stringListed("𝑎𝑏𝑐𝑑𝑒𝑓𝑔𝑕𝑖𝑗𝑘𝑙𝑚𝑛𝑜𝑝𝑞𝑟𝑠𝑡𝑢𝑣𝑤𝑥𝑦𝑧𝐴𝐵𝐶𝐷𝐸𝐹𝐺𝐻𝐼𝐽𝐾𝐿𝑀𝑁𝑂𝑃𝑄𝑅𝑆𝑇𝑈𝑉𝑊𝑋𝑌𝑍",
                    false));
            put("monospace", stringListed("𝚊𝚋𝚌𝚍𝚎𝚏𝚐𝚑𝚒𝚓𝚔𝚕𝚖𝚗𝚘𝚙𝚚𝚛𝚜𝚝𝚞𝚟𝚠𝚡𝚢𝚣𝙰𝙱𝙲𝙳𝙴𝙵𝙶𝙷𝙸𝙹𝙺𝙻𝙼𝙽𝙾𝙿𝚀𝚁𝚂𝚃𝚄𝚅𝚆𝚇𝚈𝚉",
                    false));
            put("script", stringListed("𝒶𝒷𝒸𝒹𝑒𝒻𝑔𝒽𝒾𝒿𝓀𝓁𝓂𝓃𝑜𝓅𝓆𝓇𝓈𝓉𝓊𝓋𝓌𝓍𝓎𝓏𝒜𝐵𝒞𝒟𝐸𝐹𝒢𝐻𝐼𝒥𝒦𝐿𝑀𝒩𝒪𝒫𝒬𝑅𝒮𝒯𝒰𝒱𝒲𝒳𝒴𝒵",
                    false));
            put("bold-italic", stringListed("𝒂𝒃𝒄𝒅𝒆𝒇𝒈𝒉𝒊𝒋𝒌𝒍𝒎𝒏𝒐𝒑𝒒𝒓𝒔𝒕𝒖𝒗𝒘𝒙𝒚𝒛𝑨𝑩𝑪𝑫𝑬𝑭𝑮𝑯𝑰𝑱𝑲𝑳𝑴𝑵𝑶𝑷𝑸𝑹𝑺𝑻𝑼𝑽𝑾𝑿𝒀𝒁",
                    false));
            put("bold-script", stringListed("𝓪𝓫𝓬𝓭𝓮𝓯𝓰𝓱𝓲𝓳𝓴𝓵𝓶𝓷𝓸𝓹𝓺𝓻𝓼𝓽𝓾𝓿𝔀𝔁𝔂𝔃𝓐𝓑𝓒𝓓𝓔𝓕𝓖𝓗𝓘𝓙𝓚𝓛𝓜𝓝𝓞𝓟𝓠𝓡𝓢𝓣𝓤𝓥𝓦𝓧𝓨𝓩",
                    false));
            put("double-struck", stringListed("𝕒𝕓𝕔𝕕𝕖𝕗𝕘𝕙𝕚𝕛𝕜𝕝𝕞𝕟𝕠𝕡𝕢𝕣𝕤𝕥𝕦𝕧𝕨𝕩𝕪𝕫𝔸𝔹ℂ𝔻𝔼𝔽𝔾ℍ𝕀𝕁𝕂𝕃𝕄ℕ𝕆ℙℚℝ𝕊𝕋𝕌𝕍𝕎𝕏𝕐ℤ",
                    false));
            put("sans-serif", stringListed("𝖺𝖻𝖼𝖽𝖾𝖿𝗀𝗁𝗂𝗃𝗄𝗅𝗆𝗇𝗈𝗉𝗊𝗋𝗌𝗍𝗎𝗏𝗐𝗑𝗒𝗓𝖠𝖡𝖢𝖣𝖤𝖥𝖦𝖧𝖨𝖩𝖪𝖫𝖬𝖭𝖮𝖯𝖰𝖱𝖲𝖳𝖴𝖵𝖶𝖷𝖸𝖹",
                    false));
            put("sans-serif-bold", stringListed("𝗮𝗯𝗰𝗱𝗲𝗳𝗴𝗵𝗶𝗷𝗸𝗹𝗺𝗻𝗼𝗽𝗾𝗿𝘀𝘁𝘂𝘃𝘄𝘅𝘆𝘇𝗔𝗕𝗖𝗗𝗘𝗙𝗚𝗛𝗜𝗝𝗞𝗟𝗠𝗡𝗢𝗣𝗤𝗥𝗦𝗧𝗨𝗩𝗪𝗫𝗬𝗭",
                    false));
            put("sans-serif-italic", stringListed("𝘢𝘣𝘤𝘥𝘦𝘧𝘨𝘩𝘪𝘫𝘬𝘭𝘮𝘯𝘰𝘱𝘲𝘳𝘴𝘵𝘶𝘷𝘸𝘹𝘺𝘻𝘈𝘉𝘊𝘋𝘌𝘍𝘎𝘏𝘐𝘑𝘒𝘓𝘔𝘕𝘖𝘗𝘘𝘙𝘚𝘛𝘜𝘝𝘞𝘟𝘠𝘡",
                    false));
            put("sans-serif-bold-italic", stringListed("𝙖𝙗𝙘𝙙𝙚𝙛𝙜𝙝𝙞𝙟𝙠𝙡𝙢𝙣𝙤𝙥𝙦𝙧𝙨𝙩𝙪𝙫𝙬𝙭𝙮𝙯𝘼𝘽𝘾𝘿𝙀𝙁𝙂𝙃𝙄𝙅𝙆𝙇𝙈𝙉𝙊𝙋𝙌𝙍𝙎𝙏𝙐𝙑𝙒𝙓𝙔𝙕",
                    false));
            put("reverse", stringListed("ɐqɔpǝɟƃɥᴉɾʞlɯuodbɹsʇnʌʍxʎzⱯꓭƆꓷꓱℲꓨHIꓩꞰꓶꟽNOꓒQꓤSꞱꓵɅMX⅄Z",
                    false));
            //实际有效：асԁеցһіјӏոорԛѕսԝхуАВСЕНІЈКМОРԚЅΤՍԜХΥΖ
            put("fake-normal", stringListed("аbсԁеfցһіјkӏmոорԛrѕtսvԝхуzАВСDЕFGНІЈКLМNОРԚRЅΤՍVԜХΥΖ",
                    false));
        }});
        put("numbers", new HashMap<String, List<String>>(){{
            put("normal", stringListed("0123456789", false));
            put("bold", stringListed("𝟎𝟏𝟐𝟑𝟒𝟓𝟔𝟕𝟖𝟗", false));
            put("monospace", stringListed("𝟶𝟷𝟸𝟹𝟺𝟻𝟼𝟽𝟾𝟿", false));
            put("sans-serif", stringListed("𝟢𝟣𝟤𝟥𝟦𝟧𝟨𝟩𝟪𝟫", false));
            put("double-struck", stringListed("𝟘𝟙𝟚𝟛𝟜𝟝𝟞𝟟𝟠𝟡", false));
            put("sans-serif-bold", stringListed("𝟬𝟭𝟮𝟯𝟰𝟱𝟲𝟳𝟴𝟵", false));
        }});
        put("marks", new HashMap<String, List<String>>(){{
            put("normal", Arrays.asList("\\?", "\\.", ",", "!", "\\&", "\""));
            put("reverse", Arrays.asList("¿", "˙", "'", "¡", "⅋", ",,"));
        }});
    }};

    public static final String[] fontNames = {"bold", "italic", "monospace", "script", "bold-italic",
            "bold-script", "double-struck", "sans-serif", "sans-serif-bold", "sans-serif-italic",
            "sans-serif-bold-italic", "reverse", "fake-normal"};

    public WordsAway() {

    }

    public List<String> stringListed(String text, boolean marks, String beforeMark, String afterMark) {
        List<String> list = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            text.codePoints().forEach((code) -> {
                list.add(String.valueOf(Character.toChars(code)));
            });
        }
        List<String> result = new ArrayList<>();
        if (marks) {
            boolean inMarks = false;
            int before = 0;
            for (int j = 0; j < list.size(); j++) {
                String x = list.get(j);
                if (x.equals(beforeMark)) {
                    if (inMarks) {
                        result.addAll(list.subList(before, j));
                    } else {
                        inMarks = true;
                        before = j;
                    }
                } else if (x.equals(afterMark) && inMarks) {
                    inMarks = false;
                    result.add(join(list.subList(before, j+1)));
                } else if (!inMarks){
                    result.add(x);
                }
            }
            if (inMarks) {
                result.addAll(list.subList(before, list.size()));
            }
        } else {
            result = list;
        }
        return result;
    }
    public List<String> stringListed(String text, boolean marks) {
        return stringListed(text, marks, "\ue0dc", "\ue0dd");
    }

    public String join(List list, String separator){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size() - 1; i++) {
            sb.append(list.get(i)).append(separator);
        }
        if (list.size() >= 1) {
            sb.append(list.get(list.size() - 1));
        }
        return sb.toString();
    }
    public String join(List list){
        return join(list, "");
    }

    public String mixin(String text, String mixin, boolean missMarks) {
        List list = stringListed(text, missMarks);
        return join(list, mixin);
    }
    public String mixin(String text) {
        return mixin(text, "\u200b", true);
    }

    public String rowsReverse(String text, boolean missMarks) {
        List<String> rows = Arrays.asList(text.split("\n"));
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < rows.size(); i++) {
            String row = rows.get(i);
            List<String> list = stringListed(row, missMarks);
            Collections.reverse(list);
            result.append("\u202e");
            result.append(join(list));
            if (i < rows.size() - 1) {
                result.append("\n");
            }
        }
        return toggleBrackets(result.toString(), missMarks);
    }

    public String wordsReverse(String text, boolean missMarks) {
        List<String> rows = Arrays.asList(text.split("\n"));
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < rows.size(); i++) {
            String row = rows.get(i);
            List<String> list = stringListed(row, missMarks);
            for (int j = 0; j < list.size(); j += 3) {
                String first = list.get(j);
                String second = (j + 1 < list.size()) ?
                        toggleBracketsOne(list.get(j + 1)) :
                        "";
                String third = (j + 2 < list.size()) ?
                        toggleBracketsOne(list.get(j + 2)) :
                        "";
                result.append("\u200e" + first + "\u202e" + third + second + "\u202c");
            }
            if (i < rows.size() - 1) {
                result.append("\n");
            }
        }
        return result.toString();
    }

    public String toggleBrackets(String text, boolean marks) {
        List<String> list = stringListed(text, marks);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            result.append(toggleBracketsOne(list.get(i)));
        }
        return result.toString();
    }
    public String toggleBrackets(String text) {
        return toggleBrackets(text, true);
    }
    public String toggleBracketsOne(String text) {
        switch (text) {
            case "[":
                return "]";
            case "]":
                return "[";
            case "{":
                return "}";
            case "}":
                return "{";
            case "(":
                return ")";
            case ")":
                return "(";
            case "<":
                return ">";
            case "（":
                return "）";
            case "）":
                return "（";
            case "《":
                return "》";
            case "》":
                return "《";
            case "【":
                return "】";
            case "】":
                return "【";
            case ">":
                return "<";
            default:
                return text;
        }
    }

    public String verticalText(String text, int maxCol, int minHeight) {
        text = text.replaceAll("[\\s]", "");
        int rowNum = Math.max((int)Math.ceil(text.length() / maxCol), minHeight);
        List<String> list = stringListed(text, false);
        StringBuilder[] rows = new StringBuilder[rowNum];
        for (int i = 0; i < rows.length; i++) {
            rows[i] = new StringBuilder();
        }
        for (int i = 0; i < list.size(); i++) {
            rows[i % rowNum].append(list.get(i)).append(" ");
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < rows.length; i++) {
            result.append(rows[i]).append("\n");
        }
        return result.toString();
    }
    public String verticalText(String text) {
        return verticalText(text, 12, 5);
    }

    public String replaceAll(String text, List<String> from, List<String> to, boolean missMarks) {
        StringBuilder result = new StringBuilder();
        List<String> list = stringListed(text, missMarks);
        for (int i = 0; i < list.size(); i++) {
            String x = list.get(i);
            boolean found = false;
            for (int j = 0; j < from.size(); j++) {
                if (x.equals(from.get(j))) {
                    result.append(to.get(j));
                    found = true;
                }
            }
            if (!found) {
                result.append(x);
            }
        }
        return result.toString();
    }

    public String font(String text, String from, String to, boolean missMarks) {
        for (Map fonts : styles.values()) {
            if (!(fonts.containsKey(from) && fonts.containsKey(to))) {
                continue;
            }
            text = replaceAll(text, (List<String>) fonts.get(from), (List<String>)fonts.get(to), missMarks);
        }
        return text;
    }
    public String font(String text, String to) {
        return font(text, "normal", to, true);
    }
}
