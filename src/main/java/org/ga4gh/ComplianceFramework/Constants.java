package org.ga4gh.ComplianceFramework;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Class containing constants used in the framework, to avoid hardcoding in methods.
 */
public class Constants {
    public final static Charset ENCODING_UTF8 = StandardCharsets.UTF_8;
    public final static Charset ENCODING_UTF16 = StandardCharsets.UTF_16;
    public final static Charset ENCODING_US_ASCII = StandardCharsets.US_ASCII;

    public final static String RESOURCE_DIR = "./resources/";
    public final static String RESULT_DIR = "./report/";

    public final static String SEQUENCE_ACCEPT_HEADER = "text/vnd.ga4gh.refget.v2.0.0+plain";
    public final static String INFO_ACCEPT_HEADER = "application/vnd.ga4gh.refget.v2.0.0+json";
    public final static String METADATA_ACCEPT_HEADER = "application/vnd.ga4gh.refget.v2.0.0+json";
    public final static String METADATA_RESPONSE_CONTENT_TYPE_HEADER = "application/vnd.ga4gh.refget.v2.0.0+json";

    public final static String INFO_ENDPOINT = "/sequence/service-info";
    public final static String METADATA_ENDPOINT = "/metadata";
    public final static String SEQUENCE_ENDPOINT = "/sequence/";

    public final static String NON_CIRCULAR_SEQUENCE_MD5 = "6681ac2f62509cfc220d78751b8dc524";
    public final static String NON_CIRCULAR_SEQUENCE_SHA512 = "959cb1883fc1ca9ae1394ceb475a356ead1ecceff5824ae7";
    public final static String NON_CIRCULAR_SEQUENCE_GA4GH = "ga4gh:SQ.lZyxiD_ByprhOUzrR1o1bq0ezO_1gkrn";
    public final static String CIRCULAR_SEQUENCE_MD5 = "3332ed720ac7eaa9b3655c06f6b9e196";

    public final static String REFGET_PROPERTY_CIRCULAR_SUPPORTED = "refget.circular_supported";
    public final static String REFGET_PROPERTY_ALGORITHMS = "refget.algorithms";
    public final static String REFGET_PROPERTY_SUBSEQUENCE_LIMIT = "refget.subsequence_limit";
    public final static String REFGET_PROPERTY_VERSION= "type.version";
}
