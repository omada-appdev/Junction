package com.omada.junction.data.source;

import com.google.android.gms.tasks.Task;
import com.omada.junction.data.models.external.InstituteModel;

public interface InstituteDataSource extends DataSource {
    Task<InstituteModel> getInstituteDetails();
}
