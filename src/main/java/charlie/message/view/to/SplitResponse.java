package charlie.message.view.to;

import charlie.card.Hid;
import charlie.message.Message;

/**
 * A class that is used to pass the table the hand created from split
 * 
 * @author blossom
 */
public class SplitResponse extends Message{
    
    private final Hid origHid;
    private final Hid newHid;
    
    /**
     * Constructor
     * @param newHid
     * @param origHid
     */
    public SplitResponse(Hid newHid, Hid origHid){
        this.newHid = newHid;
        this.origHid = origHid;
    }
    
    public Hid getNewHid(){
        return newHid;
    }
    
    public Hid getOrigHid(){
        return origHid;
    }
    
}