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

/**
 * Application-wide constants
 * @author Ron Coleman
 */
public class Constant {
    public final static String DIR_IMGS = "./images/";
    public final static String DIR_CARD_IMGS = "./images/cards/";
    public final static int HAND_LITERAL_VALUE = 0;
    public final static int HAND_SOFT_VALUE = 1;  
    public final static Integer DEAL_DELAY = 750;
    public final static Double PLAYER_BANKROLL = 1000.0;
    public final static Double BOT_BANKROLL = 1000.0;
    public final static Double BOT_MIN_BET = 5.0;
    public final static Integer MIN_BET = 5;
    public final static int SHOE_X = 500;
    public final static int SHOE_Y = 0;
    public final static String PLUGIN_SIDE_BET_RULE = "charlie.sidebet.rule";     
    public final static String PLUGIN_SIDE_BET_VIEW = "charlie.sidebet.view"; 
    public final static String PLUGIN_LOGAN = "charlie.bot.logan"; 
    public final static String PLUGIN_TRAP = "charlie.trap";
    public final static String PLUGIN_SHOE = "charlie.shoe";
    public final static String PLUGIN_ADVISOR = "charlie.advisor";
}
