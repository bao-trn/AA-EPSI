package states;

import geometry.FormGeo;
import states.interfaces.ShapeState;

public class SelectedState implements ShapeState {

    @Override
    public boolean getState(FormGeo formGeo){
        return true;
    }

}
