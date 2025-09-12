package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Patient")
data class PatientDataLocal(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    val patientFname: String,
    val patientLname: String,
    val patientId: Int,
    val patientGen: String,
    val patientAge: Int,
    val camp_id: Int,
    val location: String,
    val AgeUnit: String,
    val RegNo:String,
    val isSyn:Int=0



):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(_id)
        dest.writeString(patientFname)
        dest.writeString(patientLname)
        dest.writeInt(patientId)
        dest.writeString(patientGen)
        dest.writeInt(patientAge)
        dest.writeInt(camp_id)
        dest.writeString(location)
        dest.writeString(AgeUnit)
        dest.writeString(RegNo)
    }

    companion object CREATOR : Parcelable.Creator<PatientDataLocal> {
        override fun createFromParcel(parcel: Parcel): PatientDataLocal {
            return PatientDataLocal(parcel)
        }

        override fun newArray(size: Int): Array<PatientDataLocal?> {
            return arrayOfNulls(size)
        }
    }
}