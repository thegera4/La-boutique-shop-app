package com.shop.laboutique.ui.activities.models

import android.os.Parcel
import android.os.Parcelable

data class SoldProduct(
    val user_id: String? = "",
    val title: String? = "",
    val price: String? = "",
    val sold_quantity: String? = "",
    val image: String? = "",
    val order_id: String? = "",
    val order_date: Long = 0L,
    val sub_total_amount: String? = "",
    val shipping_charge: String? = "",
    val total_amount: String? = "",
    val address: Address? = Address(),
    var id: String? = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Address::class.java.classLoader),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(user_id)
        parcel.writeString(title)
        parcel.writeString(price)
        parcel.writeString(sold_quantity)
        parcel.writeString(image)
        parcel.writeString(order_id)
        parcel.writeLong(order_date)
        parcel.writeString(sub_total_amount)
        parcel.writeString(shipping_charge)
        parcel.writeString(total_amount)
        parcel.writeParcelable(address, flags)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SoldProduct> {
        override fun createFromParcel(parcel: Parcel): SoldProduct {
            return SoldProduct(parcel)
        }

        override fun newArray(size: Int): Array<SoldProduct?> {
            return arrayOfNulls(size)
        }
    }
}
