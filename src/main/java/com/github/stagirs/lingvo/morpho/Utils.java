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

/**
 *
 * @author Dmitriy Malakhov
 */
public class Utils {
    public static int char2byte(char c){
        if('а' <= c && c <= 'я'){
            return (c - 'а' + 2);
        }
        if(c == 'ё'){
            return ('е' - 'а' + 2);
        }
        if('А' <= c && c <= 'Я'){
            return (c - 'А' + 2);
        }
        if(c == 'Ё'){
            return ('Е' - 'А' + 2);
        }
        return 1;
    }
}
