/*
 Copyright (c) Ron Coleman

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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains convenience helper methods.
 * @author Ron.Coleman
 */
public class Helper {
    public static int getPropertyOrElse(String key,int dfault) {
        String value = System.getProperty(key);
        if(value != null)
            return Integer.parseInt(value);
        else
            return dfault;
    }
    
    public static String getPropertyOrElse(String key,String dfault) {
        String value = System.getProperty(key);
        if(value != null)
            return value;
        else
            return dfault;
    } 
    
    public static Boolean getPropertyOrElse(String key,Boolean dfault) {
        String value = System.getProperty(key);
        if(value != null)
            return Boolean.parseBoolean(value);
        else
            return dfault;
    }
    
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
