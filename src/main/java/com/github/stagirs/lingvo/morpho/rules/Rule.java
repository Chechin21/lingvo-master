package com.github.stagirs.lingvo.morpho.rules;
/**
 Общий интерфейс типа правило
 @author ivan
 */
public interface Rule {
    int getForm(String spl,int num);
}
