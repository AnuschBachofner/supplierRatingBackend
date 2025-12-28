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