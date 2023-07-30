package converter.accessor;

import converter.model.ConversionStatus;

public interface SQSAccessor {

    public void sendStatusUpdateMessage(String conversionId, ConversionStatus status);
}
