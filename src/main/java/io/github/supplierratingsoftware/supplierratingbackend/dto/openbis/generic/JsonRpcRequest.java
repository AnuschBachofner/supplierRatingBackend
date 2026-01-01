package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.generic;

import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisJsonConstants;

import java.util.List;
import java.util.UUID;


/**
 * Represents a standard JSON RPC 2.0 Request.
 *
 * <p><strong>Serialized JSON Example:</strong></p>
 * <pre>
 *     {
 *          "jsonrpc": "2.0",
 *          "method": "methodName",
 *          "params": []
 *          "id": "1234567890"
 *     }
 * </pre>
 *
 * @param jsonrpc The version of the JSON RPC protocol.
 * @param method  The method to invoke on openBIS.
 * @param params  The parameters to pass to the method.
 * @param id      A unique identifier for the request.
 */
public record JsonRpcRequest(String jsonrpc, String method, List<Object> params, String id) {
    /**
     * Convenience constructor that sets the jsonrpc version to the default value.
     *
     * @param method The method to invoke on openBIS.
     * @param params The parameters to pass to the method.
     */
    public JsonRpcRequest(String method, List<Object> params) {
        this(OpenBisJsonConstants.JSON_RPC_VERSION, method, params, UUID.randomUUID().toString());
    }
}
