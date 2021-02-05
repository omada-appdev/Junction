package com.omada.junction.data;

import com.omada.junction.data.DataRepository;

public abstract class BaseDataHandler {

    // Used to get data related to an accessor of this handler through data repository
    protected final DataRepository.DataRepositoryHandlerIdentifier handlerIdentifier = DataRepository.registerDataHandler();

    protected BaseDataHandler(){}

}
