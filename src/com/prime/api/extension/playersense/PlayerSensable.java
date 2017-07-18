package com.prime.api.extension.playersense;

import java.util.function.Supplier;

public interface PlayerSensable {

    String getKey();

    Supplier getInitialValue();

    Object getValue();
}
