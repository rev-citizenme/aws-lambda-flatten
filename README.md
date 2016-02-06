An Amazon Lambda function to flatten given nested int array into a single level array.

Expected:
- Input is a JSON array similar to: [1,[2,3,[4]],[5],6,[7,[8]]]
- Output is a flat JSON array similar to: [1,2,3,4,5,6,7,8]

Uses [Java Streams](http://docs.aws.amazon.com/lambda/latest/dg/java-handler-io-type-stream.html) for input and output considering recursive nature of input.

Input stream is parsed with Jackson and output is also produced with Jackson. Jackson library is packaged within resulting Jar because AWS Lamda requires a single package. Approach is described in http://docs.aws.amazon.com/lambda/latest/dg/java-create-jar-pkg-maven-and-eclipse.html please the POM.

A test class is also provided. See the following screenshot for curl ouputs for various inputs:

![curl](/doc/curl-output.png?raw=true "Curl Output")