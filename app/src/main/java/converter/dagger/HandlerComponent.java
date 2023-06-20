package converter.dagger;

import converter.accessor.S3Accessor;
import converter.filehandler.FileHandler;
import converter.filehandler.FileHandlerProvider;
import dagger.Component;

import java.util.Set;

/**
 * Dagger component that provides dependencies for AWS Lambda handlers found in this project
 */
@Component(modules = {AWSModule.class, ConverterModule.class})
public interface HandlerComponent {
    S3Accessor s3Accessor();
    FileHandlerProvider fileHandlerProvider();
    /**
     * Returns the Dagger implementation of this component
     * @return an implementation of the HandlerComponent interface
     */
    static HandlerComponent create() {
        return DaggerHandlerComponent.create();
    }
}
