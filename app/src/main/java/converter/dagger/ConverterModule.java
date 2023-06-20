package converter.dagger;

import converter.filehandler.FileHandler;
import converter.filehandler.ImageIOHandler;
import dagger.Module;
import dagger.Binds;
import dagger.multibindings.IntoSet;

/**
 * Dagger module that provides classes needed to convert files
 */
@Module
public abstract class ConverterModule {
    @Binds @IntoSet
    public abstract FileHandler imageIOHandler(ImageIOHandler imageIOHandler);
}
