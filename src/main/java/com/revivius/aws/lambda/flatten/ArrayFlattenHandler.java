package com.revivius.aws.lambda.flatten;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AWS Lambda handler that takes an arbitrary array of arrays with numbers and
 * flattens it into a single array. 
 *
 * Eg: [1,[2,3,[4]],[5]] -> [1, 2, 3, 4, 5]
 *
 * @author hamit.hasahnhocaoglu
 */
public class ArrayFlattenHandler {

    /**
     * This method is called by AWS Lambda execution environment.
     * 
     * @param inputStream nested JSON array, eg : [1,[2,3,[4]],[5],6,[7,[8]]]
     * @param outputStream flat JSON array, eg: [1,2,3,4,5,6,7,8]
     * @param context AWS Lambda execution context
     *
     * @throws IOException if input stream does not contain valid JSON string
     * @throws IllegalArgumentException input stream contains valid JSON
     * string bu unexpected structure
     */
    public void flatten(InputStream inputStream, OutputStream outputStream,
            Context context) throws IOException {
        String jsonStr;
        try (InputStreamReader is = new InputStreamReader(inputStream);
             BufferedReader buffer = new BufferedReader(is)) {
            jsonStr = buffer.lines().collect(Collectors.joining("\n"));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readValue(jsonStr, JsonNode.class);

        List<Integer> result = new ArrayList<>();
        appendNumberRecursive(node, result);
        objectMapper.writeValue(outputStream, result);
    }

    /**
     * Appands the content of given JsonNode to given result list if node
     * contains an integer or calls itself recursively if node is an array.
     * If none above holds; and IAE will be thrown indicating unexpected
     * node content.
     * 
     * @param node JsonNode
     * @param result list to append to
     * 
     * @throws IllegalArgumentException if given JsonNode is not an integer
     * or array
     */
    private void appendNumberRecursive(JsonNode node, List<Integer> result) {
        if (node.canConvertToInt()) {
            result.add(node.asInt());
        } else if (node.isArray()) {
            Iterator<JsonNode> elements = node.elements();
            while (elements.hasNext()) {
                appendNumberRecursive(elements.next(), result);
            }
        } else {
            throw new IllegalArgumentException(String.format("Expecting nested "
                    + "number arrays only: '%s' is not a number or an array!",
                    node.toString())
            );
        }
    }

}
