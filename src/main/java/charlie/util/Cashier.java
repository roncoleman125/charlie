/*
 Copyright (c) 2014 Ron Coleman

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package charlie.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Helper class for converting between chips and cash.
 * @author Ron Coleman
 */
public class Cashier {
    public enum Chip { DOLLAR, FIVE, TWENTY_FIVE, HUNDRED };
    
    private static final HashMap<Chip,Integer> exchange = new HashMap<Chip,Integer>( )
    {{
        put(Chip.DOLLAR,1);
        put(Chip.FIVE,5);
        put(Chip.TWENTY_FIVE,25);
        put(Chip.HUNDRED,100);
    }};
    
    /**
     * Doubles the number of chips.
     * @param chips Chips
     * @return 
     */
    public static List<Chip> dubble(List<Chip> chips) {
        chips.addAll(chips);
        return chips;
    }
    
    /**
     * Cashes chips
     * @param chips
     * @return Dollar value
     */
    public static Double cash(List<Chip> chips) {
        Double sum = 0.0;
        
        for(Chip chip: chips) {
            sum += exchange.get(chip);
        }
        
        return sum;
    }
    
    /**
     * Converts dollar amount to chips.
     * @param amt Dollar amount
     * @return Chips
     */
    public static List<Chip> toChips(Integer amt) {       
        List<Chip> myChips = new ArrayList<>();

        int hundreds = amt / 100;
        
        for(int k=0; k < hundreds; k++)
            myChips.add(Chip.HUNDRED);
        
        int leftover = amt - hundreds * 100;
        
        int twentyFives = leftover / 25;
        
        for(int k=0; k < twentyFives; k++)
            myChips.add(Chip.TWENTY_FIVE);
        
        leftover = amt - hundreds * 100 - twentyFives * 25;
        
        int fives = leftover / 5;
        
        for(int k=0; k < fives; k++)
            myChips.add(Chip.FIVE);        

        return myChips;
    }
}
