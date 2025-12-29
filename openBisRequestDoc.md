# OpenBis Request Documentation

## General Information about the OpenBIS-Server

**Search Requests:**
Arrays in Search Requests always have a fixed length of 3:

1. Session Token
2. Search Criteria
3. Fetch Options

## Search for Samples (Suppliers, Orders, etc.)

**Responsible Component:** `OpenBisClient` (called by Services like `SupplierService`)

**Strategy:**
We strictly use **Server-Side Filtering** based on the location of the samples (Space and Project).
Filtering by "Sample Type" proved unreliable in V3 JSON-RPC manual construction due to polymorphism complexity.
Filtering by **Space** and **Project** is precise, performant, and avoids loading unrelated data (like `TESTSPACE` or
other lab data).

### Scenario: Get All Suppliers

**Logic:**
Search for all samples located in:

* Space: `LIEFERANTENBEWERTUNG`
* Project: `LIEFERANTEN`

**JSON-RPC Request Payload (Working Production Example):**

```json
{
  "jsonrpc": "2.0",
  "method": "searchSamples",
  "id": 1,
  "params": [
    "YOUR_SESSION_TOKEN",
    {
      "@type": "as.dto.sample.search.SampleSearchCriteria",
      "operator": "AND",
      "criteria": [
        {
          "@type": "as.dto.space.search.SpaceSearchCriteria",
          "operator": "AND",
          "criteria": [
            {
              "@type": "as.dto.common.search.CodeSearchCriteria",
              "fieldValue": {
                "@type": "as.dto.common.search.StringEqualToValue",
                "value": "LIEFERANTENBEWERTUNG"
              }
            }
          ]
        },
        {
          "@type": "as.dto.project.search.ProjectSearchCriteria",
          "operator": "AND",
          "criteria": [
            {
              "@type": "as.dto.common.search.CodeSearchCriteria",
              "fieldValue": {
                "@type": "as.dto.common.search.StringEqualToValue",
                "value": "LIEFERANTEN"
              }
            }
          ]
        }
      ]
    },
    {
      "@type": "as.dto.sample.fetchoptions.SampleFetchOptions",
      "properties": {
        "@type": "as.dto.property.fetchoptions.PropertyFetchOptions"
      },
      "type": {
        "@type": "as.dto.sample.fetchoptions.SampleTypeFetchOptions"
      }
    }
  ]
}
```

### Key Learnings (V3 API 6.x)

1. **Composite Pattern:** Almost every search criteria (including `SpaceSearchCriteria` and `ProjectSearchCriteria`)
   behaves as a composite container.

   * They do **not** use direct fields like `code` or `permId` for the value.
   * Instead, they contain a `criteria` list where you must add a sub-criterion (e.g., `CodeSearchCriteria`).
   
2. **Type Info:** Jackson needs explicit `@type` information for every subclass in the polymorphic lists.
3. **String Wrappers:** Simple string values must be wrapped in `StringEqualToValue` objects inside `fieldValue`.

### Involved DTOs (Backend Implementation)

The request is built up by the following Java classes (DTOs):

**Search Criteria:**

* **`SampleSearchCriteria`** (Root Container)
    * Contains a list of `SearchCriteria`.
* **`SpaceSearchCriteria`** (Container for Space Logic)
    * Must be added to the `criteria` list of `SampleSearchCriteria`.
    * Uses `criteria` list (Composite) instead of direct fields.
* **`ProjectSearchCriteria`** (Container for Project Logic)
    * Must be added to the `criteria` list of `SampleSearchCriteria`.
* **`CodeSearchCriteria`** (The actual filter)
    * Used inside `SpaceSearchCriteria` and `ProjectSearchCriteria` to define the target name (e.g. "LIEFERANTEN").
* **`StringEqualToValue`** (The value wrapper)
    * Wraps the actual string (e.g. "LIEFERANTEN") and provides type info.

**Fetch Options:**

* **`SampleFetchOptions`** (Root Fetch Options)
    * Defines which parts of the sample to load (e.g. properties, type).
* **`PropertyFetchOptions`**
    * Enables loading of the properties map (contains business data like `NAME`, `CITY`).
* **`SampleTypeFetchOptions`**
    * Enables loading of the sample type information (e.g. code "LIEFERANT").

## Search for Orders (with Parent Supplier)

**Responsible Component:** `OrderService` -> `OpenBisClient`

**Strategy:**
Similar to suppliers, we filter by **Space** and **Project**. However, orders are "Child" objects
of Suppliers. To link an order to its supplier, we must fetch the **Parents** of the sample.

### Scenario: Get All Orders

**Logic:**
Search for all samples located in:
* Space: `LIEFERANTENBEWERTUNG` (Configurable)
* Project: `BESTELLUNGEN` (Configurable)

**Fetch Requirement:**
Load `properties` (Date, Status, etc.) **AND** `parents` (to get the Supplier ID).

**JSON-RPC Request Payload:**

```json
{
  "jsonrpc": "2.0",
  "method": "searchSamples",
  "id": "...",
  "params": [
    "SESSION_TOKEN_XXX",
    {
      "@type": "as.dto.sample.search.SampleSearchCriteria",
      "operator": "AND",
      "criteria": [
        {
          "@type": "as.dto.space.search.SpaceSearchCriteria",
          "operator": "AND",
          "criteria": [
            {
              "@type": "as.dto.common.search.CodeSearchCriteria",
              "fieldValue": {
                "@type": "as.dto.common.search.StringEqualToValue",
                "value": "LIEFERANTENBEWERTUNG"
              }
            }
          ]
        },
        {
          "@type": "as.dto.project.search.ProjectSearchCriteria",
          "operator": "AND",
          "criteria": [
            {
              "@type": "as.dto.common.search.CodeSearchCriteria",
              "fieldValue": {
                "@type": "as.dto.common.search.StringEqualToValue",
                "value": "BESTELLUNGEN"
              }
            }
          ]
        }
      ]
    },
    {
      "@type": "as.dto.sample.fetchoptions.SampleFetchOptions",
      "properties": {
        "@type": "as.dto.property.fetchoptions.PropertyFetchOptions"
      },
      "type": {
        "@type": "as.dto.sample.fetchoptions.SampleTypeFetchOptions"
      },
      "parents": {
        "@type": "as.dto.sample.fetchoptions.SampleFetchOptions",
        "properties": null,
        "type": null,
        "parents": null
      }
    }
  ]
}
```

### Basic structure of a search for orders (pseudocode):

```pseudo
`JsonRpcRequest`: {
We communicate with openBIS in the
- `JsonRPC 2.0` language and use the
- `searchSamples` method to search for samples.
Our request has an
- `Id` number so that we can recognize the response.
- We provide openBIS with three `parameters` for this purpose:
[
    - **Session token** (for authentication: provided by OpenBisClient)
    
    - **Search criteria** (FILTERING: which samples do we want?)
    {
        **`SampleSearchCriteria`:**
        We search for criteria that we combine with the logical AND.
        {
            **`SpaceSearchCriteria`:**
            We search for samples from a specific openBIS “space”.
            {
                **`CodeSearchCriteria`:**
                The openBIS “space” we are searching for has a specific code.
                {
                    **`StringEqualToValue`:**
                    The code must match the value we provide (e.g., “LIEFERANTENBEWERTUNG”).
                }
            }
        }
        AND
        {
            **`ProjectSearchCriteria`:**
            We search for samples from a specific openBIS “project.”
            {
                **`CodeSearchCriteria`:**
                The openBIS “project” we are searching for has a specific code.
                {
                    **`StringEqualToValue`:**
                    The code must match the value we provide (e.g., "BESTELLUNGEN").
                }
            }
        }
    }
    
    - **Fetch options** (LOADING: what details do we need?)
    {
        **`SampleFetchOptions`:**
        We define which parts of the sample structure to retrieve.
        fetch the properties: {
            **`PropertyFetchOptions`:**
            Load the property map (contains data like Date, Status, etc.).
        }
        fetch the type: {
            **`SampleTypeFetchOptions`:**
            Load the type information of the sample.
        }
        fetch the parents: {
            **`SampleFetchOptions`:**
            Recursively load the parents of the sample (required to link the Order to its Supplier).
        }
    }
]
}
```

### Key Learnings (Hierarchy Fetching)

1.  **Recursive FetchOptions:** The `SampleFetchOptions` object now contains a `parents` field,
    which is itself a `SampleFetchOptions` object.
2.  **Parent Depth:** In the example above, we fetch the immediate parents but set their
    properties/type/parents to `null`. This is sufficient to retrieve the parent's `permId`
    (which acts as the `supplierId` in our domain), minimizing data transfer.

### Involved DTOs (Recursive Structure)

To support the hierarchical relationship between Orders (Child) and Suppliers (Parent), the
fundamental DTOs have been extended to allow recursion.

**1. Recursive Fetch Options (`SampleFetchOptions`)**
The `SampleFetchOptions` record now includes a self-reference to fetch parents. This allows
building the nested JSON structure shown above.

**2. Recursive Result (`OpenBisSample`)**
The result object structure must mirror the fetch options to deserialize the nested data correctly.

**3. Mapping Logic (`OrderMapper`)**
The `OrderMapper` bridges the gap between the hierarchical OpenBIS structure and the flat API DTO.
]
}