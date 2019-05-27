package roman.common.cfgcenter;

import com.google.common.eventbus.Subscribe;

public interface PropertyListener {

    @Subscribe
    void handlePropertyChange(final PropertyModifiedEvent event);

}
