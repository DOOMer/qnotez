package org.doomer.qnotez.utils

import kotlinx.serialization.internal.SerialClassDescImpl
import kotlinx.serialization.KInput
import kotlinx.serialization.KOutput
import kotlinx.serialization.KSerializer
import kotlinx.serialization.KSerialClassDesc
import kotlinx.serialization.Serializer

import java.util.Date

import java.text.DateFormat
import java.text.SimpleDateFormat

const val  JSON_DATE_FORMAT = "yyyy-MM-dd--HH:mm:ss.SSS"

@Serializer(forClass = Date::class)
object DateSerializer : KSerializer<Date> {

     private val df: DateFormat = SimpleDateFormat(JSON_DATE_FORMAT)

    override fun save(output: KOutput, obj: Date) {
        output.writeStringValue(df.format(obj))
    }

    override fun load(input: KInput): Date {
        return df.parse(input.readStringValue())
    }

    override val serialClassDesc: KSerialClassDesc = SerialClassDescImpl("Date")
}