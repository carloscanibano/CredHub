package com.example.credhub;

import org.ksoap2.HeaderProperty;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

public class SDMWebRepo {
    public static HttpTransportSE androidHttpTransport;
    public static List<HeaderProperty> headerList_basic_auth;
    public static final String WS_NAMESPACE = "http://sdm_webrepo/";
    public static final String WS_METHOD_LIST = "ListCredentials";
    public static final String WS_METHOD_IMPORT = "ImportRecord";
    public static final String WS_METHOD_EXPORT = "ExportRecord";
}
