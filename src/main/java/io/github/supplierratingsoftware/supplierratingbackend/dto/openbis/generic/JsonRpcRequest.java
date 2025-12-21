package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.generic;

import java.util.List;
import java.util.UUID;


/**
 * Represents a standard JSON RPC 2.0 Request.
 *
 * <p><strong>Serialized JSON Example:</strong></p>
 * <pre>
 *     {
 *          "jsonrpc": "2.0",
 *          "method": "login",
 *          "params": [
 *              "username",
 *              "password"
 *          ]
 *          "id": "123"
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
     * Convenience Constructor that sets the jsonrpc version to 2.0
     *
     * @param method The method to invoke on openBIS.
     * @param params The parameters to pass to the method.
     */
    public JsonRpcRequest(String method, List<Object> params) {
        this("2.0", method, params, UUID.randomUUID().toString());
    }
}
