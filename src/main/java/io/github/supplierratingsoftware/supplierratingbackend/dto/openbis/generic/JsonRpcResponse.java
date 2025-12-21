package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.generic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a standard JSON RPC 2.0 Response.
 *
 * <p><strong>Success Example:</strong></p>
 * <pre>
 *     {
 *         "jsonrpc": "2.0",
 *         "result": "session-token-123",
 *         "error": null,
 *         "id": "123"
 *     }
 * </pre>
 *
 * <p><strong>Error Example:</strong></p>
 * <pre>
 *     {
 *         "jsonrpc": "2.0",
 *         "result": null,
 *         "error": {
 *             "code": -32602,
 *             "message": "Invalid params"
 *         },
 *         "id": "345"
 *     }
 * </pre>
 *
 * @param jsonrpc The version of the JSON RPC protocol.
 * @param result  The result of the request.
 * @param error   The error, if any.
 * @param id      The unique identifier for the request.
 * @param <T>     The type of the result.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record JsonRpcResponse<T>(String jsonrpc, T result, Object error, String id) {

    /**
     * Checks if the response has an error.
     *
     * @return true if the response has an error, false otherwise.
     */
    public boolean hasError() {
        return error != null;
    }
}
