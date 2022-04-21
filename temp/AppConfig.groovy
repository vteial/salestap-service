package io.wybis.surveymonster.model

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.transform.Canonical
import groovy.transform.ToString
import groovyx.gaelyk.datastore.Entity
import groovyx.gaelyk.datastore.Ignore
import groovyx.gaelyk.datastore.Key
import groovyx.gaelyk.datastore.Unindexed

@Entity(unindexed = false)
@Canonical
@ToString(includeNames = true)
public class AppConfig extends AbstractModelOne {

    @Key
    String id

    String value

    @Unindexed
    Map<String, Object> valueExtended

    String valueExtendedX

    long forUserId

    @Ignore
    User forUser

    long byUserId

    @Ignore
    User byUser

    // persistance operations

    void preUpdate(long updateBy) {
        this.updateBy = updateBy
        this.updateTime = new Date()
    }

    void prePersist(long createAndUpdateBy) {
        this.createBy = createAndUpdateBy
        this.updateBy = createAndUpdateBy
        Date now = new Date()
        this.createTime = now;
        this.updateTime = now;
    }

    void deserializeFromJson(ObjectMapper jsonObjectMapper) {
        try {
            if (valueExtendedX) {
                valueExtended = jsonObjectMapper.readValue(valueExtendedX, new TypeReference<Map<String, Object>>() {})
            }
        } catch (Throwable t) {
            t.printStackTrace()
        }
    }

    void serializeToJson(ObjectMapper jsonObjectMapper) {
        try {
            if (valueExtended) {
                StringWriter sw = new StringWriter()
                jsonObjectMapper.writeValue(sw, value)
                valueExtendedX = sw.toString()
            }
        } catch (Throwable t) {
            t.printStackTrace()
        }
    }
}
