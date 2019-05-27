package roman.common.cfgcenter;

import lombok.*;


@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class PropertyModifiedEvent {

    private final String propertyName;
    private final String oldValue;
    private final String newValue;

}
