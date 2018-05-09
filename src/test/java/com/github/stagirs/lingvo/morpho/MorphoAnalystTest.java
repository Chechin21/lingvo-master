/*
 * Copyright 2017 Dmitriy Malakhov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.stagirs.lingvo.morpho;

import com.github.stagirs.lingvo.morpho.model.Morpho;
import com.github.stagirs.lingvo.morpho.model.PredictedMorpho;
import java.io.IOException;
import org.apache.commons.codec.language.DaitchMokotoffSoundex;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Проверяем, правильно ли работает на словах из словаря
 * @author Dmitriy Malakhov
 */
public class MorphoAnalystTest {
    
    
    @Test
    public void findTest() throws IOException{
        MorphoIterator iterator = new MorphoIterator();
        while(iterator.hasNext()){
            Morpho m = iterator.next();
            Morpho morpho = MorphoAnalyst.find(m.getWord());
            if(morpho.getNormCount() != m.getNormCount()){
                System.out.println(m.getWord());
            }
        }
    }
    
    @Test
    public void predictTest() throws IOException{
        //https://yandex.ru/company/researches/2012/ya_orfo
        assertEquals(MorphoAnalyst.predict("карова").getDictionaryWord(), "корова");
        assertEquals(MorphoAnalyst.predict("однокласники").getDictionaryWord(), "одноклассники");
        //assertEquals(MorphoAnalyst.predict("тайланд").getDictionaryWord(), "таиланд");
        assertEquals(MorphoAnalyst.predict("агенство").getDictionaryWord(), "агентство");
        //assertEquals(MorphoAnalyst.predict("расчитать").getDictionaryWord(), "рассчитать");
        assertEquals(MorphoAnalyst.predict("зделать").getDictionaryWord(), "сделать");
        assertEquals(MorphoAnalyst.predict("отзовы").getDictionaryWord(), "отзывы");
        //assertEquals(MorphoAnalyst.predict("програма").getDictionaryWord(), "программа");
        assertEquals(MorphoAnalyst.predict("скачять").getDictionaryWord(), "скачать");
        assertEquals(MorphoAnalyst.predict("рассписание").getDictionaryWord(), "расписание");
        //assertEquals(MorphoAnalyst.predict("росии").getDictionaryWord(), "россии");
        assertEquals(MorphoAnalyst.predict("скочать").getDictionaryWord(), "скачать");
        //assertEquals(MorphoAnalyst.predict("руский").getDictionaryWord(), "русский");
        assertEquals(MorphoAnalyst.predict("поликлинника").getDictionaryWord(), "поликлиника");
        //assertEquals(MorphoAnalyst.predict("руссификатор").getDictionaryWord(), "русификатор");
        assertEquals(MorphoAnalyst.predict("офицальный").getDictionaryWord(), "официальный");
    }
    
}
