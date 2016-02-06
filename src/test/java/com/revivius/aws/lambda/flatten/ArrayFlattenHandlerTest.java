package com.revivius.aws.lambda.flatten;

import com.fasterxml.jackson.core.JsonParseException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Amazon Gateway API will make sure we will have a string conforming to
 * application/json content type so tests here moslt for ensuring correct
 * content. We could use JSON schema for this.
 * 
 * Tests ArrayFlattenHandler with valid/invalid JSON string and valid/invalid
 * input structures.
 *
 * @author hamit.hasanhocaoglu
 */
public class ArrayFlattenHandlerTest {

    @Test
    public void testWithValidInput() throws Exception {
        System.out.println("Testing with valid JSON and valid input");
        
        InputStream input = new ByteArrayInputStream("[1, [2, 3, [4]], [5], 6, [7, [8]]]".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        ArrayFlattenHandler instance = new ArrayFlattenHandler();
        instance.flatten(input, output, null);

        String actual = output.toString("UTF-8");
        String expected = "[1,2,3,4,5,6,7,8]";
        
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithValidJsonButInvalidInput() throws Exception {
        System.out.println("Testing with valid JSON but invalid input");

        InputStream input = new ByteArrayInputStream("[\"string\",[2,3,[4]],[5],6,[7,[8]]]".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        ArrayFlattenHandler instance = new ArrayFlattenHandler();
        instance.flatten(input, output, null);
    }
    
    @Test(expected = JsonParseException.class)
    public void testWithInvalidJson() throws Exception {
        System.out.println("Testing with invalid JSON");

        InputStream input = new ByteArrayInputStream("{1,[2,3,[4]],[5],6,[7,[8]]]".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        ArrayFlattenHandler instance = new ArrayFlattenHandler();
        instance.flatten(input, output, null);
    }

}
