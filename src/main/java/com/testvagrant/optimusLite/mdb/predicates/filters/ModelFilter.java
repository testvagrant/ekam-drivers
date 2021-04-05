package com.testvagrant.optimusLite.mdb.predicates.filters;



import com.testvagrant.optimusLite.commons.entities.DeviceDetails;
import com.testvagrant.optimusLite.mdb.enums.Models;
import com.testvagrant.optimusLite.mdb.predicates.MobileFilters;

import java.util.function.Predicate;

public class ModelFilter extends MobileFilters {


    public static Predicate<DeviceDetails> ofModel(Models model) {
        return deviceDetails -> deviceDetails.getDeviceName().startsWith(model.getModelName());
    }
}
