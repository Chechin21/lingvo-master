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
import com.github.stagirs.lingvo.morpho.model.Rule;
import com.github.stagirs.lingvo.morpho.model.Node;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author Dmitriy Malakhov
 */
public class MorphoIterator implements Iterator<Morpho>{
    
    private class State{
        Node node;
        int childNumber = -1;
        Iterator<Map.Entry<String, Rule>> iterator;

        public State(Node node) {
            this.node = node;
            this.iterator = node.getRules().entrySet().iterator();
        }
        
    }
    
    Stack<State> stack = new Stack();

    public MorphoIterator() {
        stack.add(new State(MorphoAnalyst.node));
    }
    
    Morpho next;    

    @Override
    public boolean hasNext() {
        if(stack.size() > 0){
            State state = stack.peek();
            if(state.iterator != null && state.iterator.hasNext()){
                Map.Entry<String, Rule> e = state.iterator.next();
                next = new Morpho(e.getKey() + state.node.getSuf(), e.getValue());
                return true;
            }
            state.iterator = null;
            state.childNumber++;
            for(;state.node.getNodes() != null && state.childNumber < state.node.getNodes().length; state.childNumber++){
                if(state.node.getNodes()[state.childNumber] == null){
                    continue;
                }
                stack.push(new State(state.node.getNodes()[state.childNumber]));
                return hasNext();
            }
            stack.pop();
            return hasNext();
        }
        next = null;
        return false;
    }

    @Override
    public Morpho next() {
        return next;
    }
    
}
