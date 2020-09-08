package top.mrys.vertx.http.constants;

import io.vertx.core.http.HttpMethod;
import lombok.Getter;

/**
 * @author mrys
 * @date 2020/7/4
 */
@Getter
public enum EnumHttpMethod {
    NONE(null),
    GET(HttpMethod.GET),
    POST(HttpMethod.POST),
    ;

    private final HttpMethod httpMethod;

    EnumHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }
}
