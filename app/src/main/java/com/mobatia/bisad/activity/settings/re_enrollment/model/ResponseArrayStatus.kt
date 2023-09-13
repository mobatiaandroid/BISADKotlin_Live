package com.mobatia.bisad.activity.settings.re_enrollment.model

import com.google.gson.annotations.SerializedName

class ResponseArrayStatus (
    @SerializedName("students") var students:ArrayList<StudentsEnrollList>
)