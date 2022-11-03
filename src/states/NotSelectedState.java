package states;

import geometry.FormGeo;
import states.interfaces.ShapeState;

public class NotSelectedState implements ShapeState {

    @Override
    public boolean getState(FormGeo formGeo){
        return false;
    }
    
}
