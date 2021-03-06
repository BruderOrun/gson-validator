package org.yongzhez.gsonvalidator;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by youngz on 24/07/16.
 */
public abstract class BaseValidator implements Validator {

    protected boolean valid;
    protected TypeValidator typeValidator;

    public BaseValidator() {
        this.typeValidator = new TypeValidator();
    }

    /**
     * @param json
     * @param schema
     */
    protected void validEnum(JsonElement json, JsonObject schema){
        for (JsonElement enumReq : schema.get("enum").getAsJsonArray()) {
            if (enumReq.equals(json)) {
                valid = true;
                return;
            }
        }
        valid = false;
    }

    /**
     * @param json
     * @param schema
     */
    protected void allOf(JsonElement json, JsonObject schema) {
        for (JsonElement req : schema.get("allOf").getAsJsonArray()) {
            if (!this.validator(json, req.getAsJsonObject())) {
                this.valid = false;
                return;
            }
        }
    }

    /**
     * @param json
     * @param schema
     */
    protected void anyOf(JsonElement json, JsonObject schema) {
        for (JsonElement req : schema.get("anyOf").getAsJsonArray()) {
            if (this.validator(json, req.getAsJsonObject())) {
                this.valid = true;
                return;
            }
        }
        this.valid = false;

    }

    /**
     * @param json
     * @param schema
     */
    protected void oneOf(JsonElement json, JsonObject schema) {
        int count = 0;

        for (JsonElement req : schema.get("oneOf").getAsJsonArray()) {
            if (this.validator(json, req.getAsJsonObject())) {
                count++;
            }
            if (count > 1) {
                break;
            }
        }
        this.valid = count == 1;
    }

    /**
     * @param json
     * @param schema
     */
    protected void not(JsonElement json, JsonObject schema) {
        valid = !this.validator(json, schema.get("not").getAsJsonObject());
    }

    protected void setValid(boolean valid) {
        this.valid = valid;
    }
}
