package converter.dagger;

import converter.accessor.S3Accessor;
import dagger.Component;

/**
 * Dagger component that provides dependencies for AWS Lambda handlers found in this project
 */
@Component(modules = AWSModule.class)
public interface HandlerComponent {
    S3Accessor s3Accessor();

    /**
     * Returns the Dagger implementation of this component
     * @return an implementation of the HandlerComponent interface
     */
    static HandlerComponent create() {
        return DaggerHandlerComponent.create();
    }
}
