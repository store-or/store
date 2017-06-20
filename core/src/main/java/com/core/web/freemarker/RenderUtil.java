package com.core.web.freemarker;

import org.codehaus.jackson.node.ObjectNode;
import org.springframework.ui.Model;

/**
 * Created by laizy on 2017/6/7.
 */
public abstract class RenderUtil {

    public static RenderUtil FREEMARKER_RENDER = new FreemarkerRender();

    public abstract ObjectNode renderWithLightId(String name, Model model) throws Exception ;

    public abstract ObjectNode render(String name, Model model) throws Exception;

    protected String between(String source, String lightTableId) {
        int stack = 0;
        char[] sourceChar = source.toCharArray();
        char[] begin = ("<light:table id=\"" + lightTableId + "\">").toCharArray();
        char[] mix = ("<light:table id=").toCharArray();
        char[] end = ("</light:table>").toCharArray();
        int beginIndex = 0;
        int endIndex = 0;
        for (int i = 0; i < sourceChar.length;) {
            //begin
            boolean match = true;
            if (stack == 0) {
                for (int j = 0; j < begin.length; j++) {
                    if (sourceChar[i + j] != begin[j]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    i = i + begin.length;
                    if (stack++ == 0) {
                        beginIndex = i;
                    }
                    continue;
                }
            }
            // 捣乱的
            match = true;
            if (stack > 0) {
                for (int j = 0; j < mix.length; j++) {
                    if (sourceChar[i + j] != begin[j]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    stack++;
                    for (int j = i + mix.length; j < sourceChar.length; j++) {
                        if (sourceChar[j] == '>') {
                            i = j+1;
                            break;
                        }
                    }
                }
            }
            if (stack > 0) {
                match = true;
                for (int j = 0; j < end.length; j++) {
                    if (sourceChar[i + j] != end[j]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    if (--stack == 0) {
                        endIndex = i;
                        break;
                    }
                    i = i + end.length;
                    continue;
                }
            }
            i++;
        }
        return source.substring(beginIndex, endIndex);
    }

}
