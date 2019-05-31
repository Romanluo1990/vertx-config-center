package roman.common.cfgcenter;

import java.util.function.Predicate;

public class KeyMatchPropertyListener implements PropertyListener{

    private PropertyListener propertyListener;

    private Predicate<String> keyMatch;

    public KeyMatchPropertyListener(Predicate<String> keyMatch, PropertyListener propertyListener) {
        this.keyMatch = keyMatch;
        this.propertyListener = propertyListener;
    }

    @Override
    public void handlePropertyChange(PropertyModifiedEvent event) {
        if(keyMatch.test(event.getPropertyName()))
            propertyListener.handlePropertyChange(event);
    }
}
