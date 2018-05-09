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
package com.github.stagirs.lingvo.morpho.model;

import com.github.stagirs.lingvo.morpho.StringStore;

/**
 *
 * @author Dmitriy Malakhov
 */
public class RuleItem {
    private String pre;
    private String suf;
    private Form norm;
    private Form raw;

    public RuleItem(String pre, String suf, Form norm, Form raw) {
        this.pre = StringStore.get(pre);
        this.suf = StringStore.get(suf);
        this.norm = norm;
        this.raw = raw;
    }

    public String getPre() {
        return pre;
    }

    public String getSuf() {
        return suf;
    }

    public Form getNorm() {
        return norm;
    }

    public Form getRaw() {
        return raw;
    }
}
